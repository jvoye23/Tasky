@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_details

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.AttendeeManager
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.domain.ImageManager
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.getNextHalfMarkLocalTime
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toEpochMilliseconds
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toLocalDateTime
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.core.domain.SessionStorage
import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.model.PhotoGridItem
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.domain.model.detailsAsEvent
import com.jvoye.tasky.core.domain.model.toPhotoGridItemTwo
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import com.jvoye.tasky.core.presentation.designsystem.util.textAsFlow
import com.jvoye.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.UUID.randomUUID
import kotlin.time.ExperimentalTime

class AgendaDetailScreenViewModel(
    private val isEdit: Boolean,
    private val taskyType: TaskyType,
    private val taskyItemId: String?,
    private val agendaRepository: AgendaRepository,
    private val savedStateHandle: SavedStateHandle,
    private val imageManager: ImageManager,
    private val attendeeManager: AttendeeManager,
    private val userValidator: UserDataValidator,
    private val sessionStorage: SessionStorage
): ViewModel() {
    private val _state = MutableStateFlow(AgendaDetailState(
        titleText = savedStateHandle["titleText"],
        descriptionText = savedStateHandle["descriptionText"],
        isEditMode = savedStateHandle.get<Boolean>("isEditMode") ?: false,
        selectedDateMillis = savedStateHandle.get<Long>("selectedDateMillis") ?: getNextHalfMarkLocalTime().toEpochMilliseconds(),
        notificationType = savedStateHandle.get<NotificationType>("notificationType") ?: NotificationType.THIRTY_MINUTES_BEFORE
    ))

    private val eventChannel = Channel<AgendaDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                val restoredItemId = savedStateHandle.get<String>("itemId")
                if (restoredItemId != null) {
                    _state.update { it.copy(
                         taskyItem = it.taskyItem.copy(
                             id = restoredItemId
                         )
                    )}
                } else {
                    // If not available in SavedStateHandle, loading data from repository
                    getTaskyItemFromRepo(taskyType, taskyItemId)
                    savedStateHandle["itemId"] = taskyItemId

                }
                getSessionUserId()
                getTypeAndEditMode(isEdit, taskyType)
                validateAttendeeEmailInput()

                hasLoadedInitialData = true
            }
        }
        .onEach { state ->
            savedStateHandle["titleText"] = state.titleText
            savedStateHandle["descriptionText"] = state.descriptionText
            savedStateHandle["isEditMode"] = state.isEditMode
            savedStateHandle["selectedDateMillis"] = state.selectedDateMillis
            savedStateHandle["notificationType"] = state.notificationType
            savedStateHandle["selectedToDateMillis"] = state.selectedToDateMillis
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    private suspend fun getTaskyItemFromRepo(taskyType: TaskyType, taskyItemId: String?) {
        if (taskyItemId == null) return

        val itemFromRepo = agendaRepository.getTaskyItem(taskyType, taskyItemId)
        _state.update { it.copy(
            taskyItem = itemFromRepo,
            titleText = itemFromRepo.title,
            descriptionText = itemFromRepo.description,
            time = itemFromRepo.time,
            toTime = itemFromRepo.detailsAsEvent()?.toTime!!,
            remindAt = itemFromRepo.remindAt,
            allAttendees = itemFromRepo.detailsAsEvent()?.eventAttendees ?: emptyList(),
            remotePhotos = itemFromRepo.detailsAsEvent()?.remotePhotos?.map { it.url } ?: emptyList(),
            remotePhotoInfos = itemFromRepo.detailsAsEvent()?.remotePhotos ?: emptyList(),
            photoGridItems = itemFromRepo.detailsAsEvent()?.remotePhotos?.map { it.toPhotoGridItemTwo() } ?: emptyList(),
            selectedDateMillis = itemFromRepo.time.toEpochMilliseconds(),
            selectedToDateMillis = itemFromRepo.detailsAsEvent()?.toTime?.toEpochMilliseconds() ?: 0,
            notificationType = itemFromRepo.notificationType,
            host = itemFromRepo.detailsAsEvent()?.host,
            isUserEventCreator = itemFromRepo.detailsAsEvent()?.isUserEventCreator ?: false,
            isGoing = itemFromRepo.detailsAsEvent()?.isGoing ?: false
        ) }
    }


    private fun getTypeAndEditMode(editMode: Boolean, type: TaskyType){
        _state.update { it.copy(
            isEditMode = editMode,
            taskyItem = it.taskyItem.copy(
                type = type
            )
        ) }
    }

    private suspend fun getSessionUserId(){
        val id = sessionStorage.get()?.userId ?: ""
        _state.update { it.copy(
            currentSessionUserId = id
        ) }
    }

    private fun saveTaskyItem() {
        _state.update { it.copy(
            isSavingTaskyItem = true
        ) }

        viewModelScope.launch {
            val isTaskyItemIdBlank  = _state.value.taskyItem.id.isBlank()
            val newTaskyItemId = _state.value.taskyItem.id.ifBlank {
                randomUUID().toString()
            }

            val eventDetails = TaskyItemDetails.Event(
                toTime = state.value.toTime,
                eventAttendees = _state.value.eventAttendees,
                lookupAttendees = _state.value.lookupAttendees,
                photos = _state.value.newLocalPhotoInfos ,
                isUserEventCreator = true,
                host = sessionStorage.get()?.userId ?: "",
                remotePhotos = _state.value.remotePhotoInfos,
                newPhotosKeys = _state.value.newLocalPhotoInfos.map { it.localPhotoKey },
                deletedPhotoKeys = _state.value.deletedPhotoKeys,
                isGoing = _state.value.isGoing,
            )

            val taskDetails = TaskyItemDetails.Task(
                isDone = _state.value.isDone
            )

            val details = when(_state.value.taskyItem.type) {
                TaskyType.EVENT -> eventDetails
                TaskyType.TASK -> taskDetails
                TaskyType.REMINDER -> TaskyItemDetails.Reminder
            }

            val taskyItem = TaskyItem(
                title = _state.value.titleText ?: "",
                description = _state.value.descriptionText ?: "",
                time = _state.value.time,
                id = if (isTaskyItemIdBlank) newTaskyItemId else _state.value.taskyItem.id,
                type = _state.value.taskyItem.type,
                remindAt = _state.value.remindAt,
                details = details,
                notificationType = _state.value.notificationType,
            )

            when(val result = if (isTaskyItemIdBlank) {
                agendaRepository.upsertTaskyItem(taskyItem)
            } else {
                agendaRepository.updateTaskyItem(taskyItem)
            }
            ) {
                is Result.Error -> {
                    eventChannel.send(AgendaDetailEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(AgendaDetailEvent.TaskyItemSaved)
                }
            }
        }
        _state.update { it.copy(
            isSavingTaskyItem = false
        ) }
    }

    private fun deleteTaskyItem() {
        _state.update { it.copy(
            isDeletingTaskyItem = true
        ) }

        viewModelScope.launch {
            when(val result = agendaRepository.deleteTaskyItem(
                taskyItemId = _state.value.taskyItem.id,
                taskyType = _state.value.taskyItem.type
            )) {
                is Result.Error -> {
                    eventChannel.send(AgendaDetailEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(AgendaDetailEvent.TaskyItemDeleted)
                }
            }
        }
        _state.update { it.copy(
            isDeletingTaskyItem = false
        ) }
    }

    private fun onImagesSelected(uris: List<Uri>) {
        viewModelScope.launch {
            when (val result = imageManager.compressImages(uris.map { it.toString() })) {
                is Result.Success -> {
                    val newLocalPhotoPaths = result.data
                    val deferredLocalPhotoInfos = newLocalPhotoPaths.mapIndexed { index, localPhotoPath ->
                        async {
                            imageManager.filePathToLocalPhotoInfo(index = index, filePath = localPhotoPath)
                        }
                    }
                    // Wait for all async jobs to complete
                    val newLocalPhotoInfos: List<LocalPhotoInfo> = deferredLocalPhotoInfos.awaitAll()
                    _state.update {
                        it.copy(
                            newLocalPhotoInfos = it.newLocalPhotoInfos + newLocalPhotoInfos,
                            photoGridItems  = it.photoGridItems + newLocalPhotoInfos.map { it.toPhotoGridItemTwo() }

                        )
                    }
                }

                is Result.Error -> {
                    eventChannel.send(AgendaDetailEvent.Error(result.error.asUiText()))
                }
            }
        }
    }

    private fun validateAttendeeEmailInput() {
        _state.value.emailInput.textAsFlow()
            .onEach { email ->
                val isValidFormat = userValidator.isValidEmail(email.toString())

                if (isValidFormat){
                    val result = attendeeManager.fetchLookupAttendee(email.toString())
                    when(result) {
                        is Result.Error -> {
                            val errorMessage = when(result.error) {
                                DataError.Network.NOT_FOUND -> UiText.StringResource(R.string.error_user_not_found)
                                DataError.Network.CONFLICT -> UiText.StringResource(R.string.same_user_not_allowed)
                                DataError.Network.BAD_REQUEST -> UiText.StringResource(R.string.invalid_email_format)
                               else -> result.error.asUiText()
                            }

                            _state.update { it.copy(
                                emailErrorText = errorMessage,
                                doesEmailExist = false,
                                validEmail = ""

                            ) }
                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    doesEmailExist = true,
                                    validEmail = result.data.email,
                                    invitedAttendee = result.data,
                                    emailErrorText = null

                                )
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AgendaDetailAction) {
        when (action) {

            AgendaDetailAction.OnEditModeClick -> {
                _state.update { it.copy(
                    isEditMode = true
                ) }
            }

            AgendaDetailAction.OnSaveClick -> {
                saveTaskyItem()
            }
            AgendaDetailAction.OnDeleteClick -> {
                deleteTaskyItem()
            }

            AgendaDetailAction.OnToggleTimePickerDialog -> {
                _state.update { it.copy(
                    isTimePickerDialogVisible = !it.isTimePickerDialogVisible
                ) }
            }

            AgendaDetailAction.OnToggleDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogVisible = !it.isDatePickerDialogVisible
                ) }
            }

            AgendaDetailAction.OnToggleToTimePickerDialog -> {
                _state.update { it.copy(
                    isToTimePickerDialogVisible = !it.isToTimePickerDialogVisible
                ) }
            }

            AgendaDetailAction.OnToggleToDatePickerDialog -> {
                _state.update { it.copy(
                    isToDatePickerDialogVisible = !it.isToDatePickerDialogVisible
                ) }
            }

            is AgendaDetailAction.ConfirmDateSelection -> {
                val currentSelectedTime = _state.value.time.time
                val newDate = action.selectedDateMillis.toLocalDateTime().date
                val newLocalDateTime = LocalDateTime(
                    year = newDate.year,
                    month = newDate.month,
                    day = newDate.day,
                    hour = currentSelectedTime.hour,
                    minute = currentSelectedTime.minute,
                    second = 0,
                    nanosecond = 0
                )
                _state.update { it.copy(
                    selectedDateMillis = newLocalDateTime.toEpochMilliseconds(),
                    time = newLocalDateTime,
                    isDatePickerDialogVisible = false
                ) }
            }

            is AgendaDetailAction.ConfirmToDateSelection -> {
                val currentSelectedToTime = _state.value.toTime.time
                val newDate = action.toDateSelectedDateMillis.toLocalDateTime().date
                val newLocalDateTime = LocalDateTime(
                    year = newDate.year,
                    month = newDate.month,
                    day = newDate.day,
                    hour = currentSelectedToTime.hour,
                    minute = currentSelectedToTime.minute,
                    second = 0,
                    nanosecond = 0
                )
                _state.update { it.copy(
                    selectedToDateMillis = newLocalDateTime.toEpochMilliseconds(),
                    toTime = newLocalDateTime,
                    isToDatePickerDialogVisible = false
                ) }
            }

            is AgendaDetailAction.ConfirmTimeSelection -> {
                val newHour = action.timePickerState.hour
                val newMinute = action.timePickerState.minute
                val newSecond = 0
                val currentDate = _state.value.time.date
                val newLocalDateTime = LocalDateTime(
                    year = currentDate.year,
                    month = currentDate.month,
                    day = currentDate.day,
                    hour = newHour,
                    minute = newMinute,
                    second = newSecond
                )
                _state.update { it.copy(
                    selectedDateMillis = newLocalDateTime.toEpochMilliseconds(),
                    time = newLocalDateTime,
                    isTimePickerDialogVisible = false
                ) }
            }

            is AgendaDetailAction.ConfirmToTimeSelection -> {
                val toHour = action.toTimePickerState.hour
                val toMinute = action.toTimePickerState.minute
                val toSecond = 0
                val toDate = _state.value.toTime.date
                val toTime = LocalDateTime(
                    year = toDate.year,
                    month = toDate.month,
                    day = toDate.day,
                    hour = toHour,
                    minute = toMinute,
                    second = toSecond
                )
                _state.update { it.copy(
                    selectedToDateMillis = toTime.toEpochMilliseconds(),
                    toTime = toTime,
                    isToTimePickerDialogVisible = false
                ) }
            }

            AgendaDetailAction.OnToggleNotificationDropdown -> {
                _state.update { it.copy(
                    isNotificationDropdownExpanded = !it.isNotificationDropdownExpanded
                ) }
            }

            is AgendaDetailAction.OnNotificationItemClick -> {
                val time = _state.value.time
                val timeInstant = time.toInstant(TimeZone.currentSystemDefault())
                val remindAt = (timeInstant - action.notificationType.offset)
                    .toLocalDateTime(TimeZone.currentSystemDefault())

                _state.update {
                    it.copy(
                        remindAt = remindAt,
                        notificationType = action.notificationType,
                        isNotificationDropdownExpanded = false
                    )
                }
            }
            AgendaDetailAction.OnToggleDeleteBottomSheet -> {
                _state.update { it.copy(
                    isDeleteBottomSheetVisible = !it.isDeleteBottomSheetVisible
                ) }
            }

            is AgendaDetailAction.OnEditTextChanged -> {
                when(action.editTextType) {
                    EditTextType.TITLE -> {
                        _state.update { it.copy(
                            titleText = action.value
                        ) }
                    }
                    EditTextType.DESCRIPTION -> {
                        _state.update { it.copy(
                            descriptionText = action.value
                        ) }
                    }
                }
            }
            is AgendaDetailAction.OnAddLocalPhotos -> {
                onImagesSelected(action.photos)
            }

            AgendaDetailAction.OnToggleAddAttendeeBottomSheet -> {
                _state.update { it.copy(
                    isAddAttendeeBottomSheetVisible = !it.isAddAttendeeBottomSheetVisible,
                ) }
            }

            is AgendaDetailAction.OnAddAttendee -> {
                val invitedUser = _state.value.invitedAttendee ?: return
                _state.update {
                    it.copy(
                        lookupAttendees = it.lookupAttendees + invitedUser,
                        allAttendees = it.allAttendees + invitedUser,
                        isAddAttendeeBottomSheetVisible = false,
                        invitedAttendee = null,

                    )
                }
            }
            is AgendaDetailAction.OnDeleteAttendee -> {
                val currentAttendees = state.value.allAttendees
                _state.update { it.copy(
                    allAttendees = currentAttendees - action.attendeeBase
                ) }

            }

            is AgendaDetailAction.OnChangeAttendeeFilter -> {
                _state.update { it.copy(
                    attendeeFilter = action.attendeeFilter
                ) }
            }

            is AgendaDetailAction.OnDeletePhoto -> {
                val photos = _state.value.photoGridItems
                val item = photos[action.photoIndex]
                when(item) {
                    is PhotoGridItem.Remote -> {
                        _state.update {
                            it.copy(
                                deletedPhotoKeys = it.deletedPhotoKeys + item.key,
                                photoGridItems = it.photoGridItems - item
                            )
                        }
                    }
                    is PhotoGridItem.Local -> {
                        _state.update { it.copy(
                            photoGridItems = it.photoGridItems - item
                        ) }
                    }
                }
            }
            is AgendaDetailAction.OnToggleEventVisit -> {
                _state.update { it.copy(
                    isGoing = !it.isGoing
                ) }
            }

            else -> Unit
        }
    }
}
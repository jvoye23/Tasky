@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda.presentation.agenda_details

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaDetailDatePicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDeleteBottomSheet
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailNotificationDropdown
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailTimePicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailTopAppBar
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toDatePickerString
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toLocalDateTime
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailPhotoPicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeSection
import com.jvoye.tasky.agenda.presentation.event_photo.EditPhotoAction
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDateTimePicker
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Awaiting
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Chevron_Right
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.ui.ObserveAsEvents
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun AgendaDetailScreenRoot(
    modifier: Modifier = Modifier,
    onCloseAndCancelClick: () -> Unit,
    onEditTextClick: (String?, EditTextType) -> Unit,
    onEditPhotoClick: (localPhotoPath: String?, photoUrl: String?) -> Unit,
    viewModel: AgendaDetailScreenViewModel = koinViewModel()
) {
    val context = LocalContext.current


    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is AgendaDetailEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            AgendaDetailEvent.TaskyItemSaved -> {
                Toast.makeText(
                    context,
                    R.string.tasky_item_saved,
                    Toast.LENGTH_LONG
                ).show()
                onCloseAndCancelClick()
            }

            AgendaDetailEvent.TaskyItemDeleted -> {
                Toast.makeText(
                    context,
                    R.string.tasky_item_deleted,
                    Toast.LENGTH_LONG
                ).show()
                onCloseAndCancelClick()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaDetailScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is AgendaDetailAction.OnCloseAndCancelClick -> onCloseAndCancelClick()
                is AgendaDetailAction.OnEditTextClick -> onEditTextClick(action.text, action.editTextType)
                is AgendaDetailAction.OnPhotoClick -> onEditPhotoClick(action.localPhotoPath, action.photoUrl)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun AgendaDetailScreen(
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit
    ) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.selectedDateMillis
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            AgendaItemDetailTopAppBar(
                isEditMode = state.isEditMode,
                taskyItem = state.taskyItem,
                onAction = onAction
            )
        }
    ) { innerPadding ->
        AgendaDetailScreenContent(
            modifier = Modifier
                .padding(innerPadding),
            state = state,
            onAction = onAction,
            datePickerState = datePickerState
        )
    }
}

@Composable
private fun AgendaDetailScreenContent(
    modifier: Modifier = Modifier,
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
    datePickerState: DatePickerState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 24.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = when (state.taskyItem.type) {
                                TaskyType.EVENT -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                                TaskyType.TASK -> MaterialTheme.colorScheme.secondary
                                TaskyType.REMINDER -> MaterialTheme.colorScheme.surfaceHigher.copy(
                                    alpha = 0.8f
                                )
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = state.taskyItem.type.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

            }
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        if (state.isEditMode) onAction(
                            AgendaDetailAction.OnEditTextClick(
                                text = state.titleText,
                                editTextType = EditTextType.TITLE
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icon_Awaiting,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = state.titleText ?: stringResource(R.string.title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icon_Chevron_Right,
                    contentDescription = null,
                    tint = if (state.isEditMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surfaceHigher)

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        if (state.isEditMode) onAction(
                            AgendaDetailAction.OnEditTextClick(
                                text = state.descriptionText,
                                editTextType = EditTextType.DESCRIPTION
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = state.descriptionText ?: stringResource(R.string.description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icon_Chevron_Right,
                    contentDescription = null,
                    tint = if (state.isEditMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surfaceHigher)

            )
            when(state.taskyItem.type) {
                TaskyType.TASK, TaskyType.REMINDER -> TaskReminderDetailsContent(
                    isEditMode = state.isEditMode,
                    onAction = onAction,
                    state = state,
                    datePickerState = datePickerState
                )
                TaskyType.EVENT -> EventDetailsContent(
                    isEditMode = state.isEditMode,
                    onAction = onAction,
                    state = state,
                    datePickerState = datePickerState
                )
            }
            

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
                .clickable { onAction(AgendaDetailAction.OnToggleDeleteBottomSheet) }
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surfaceHigher)
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp),
                text = when(state.taskyItem.type){
                    TaskyType.EVENT -> stringResource(R.string.delete_event)
                    TaskyType.TASK -> stringResource(R.string.delete_task)
                    TaskyType.REMINDER -> stringResource(R.string.delete_reminder)
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (state.isDeleteBottomSheetVisible) {
            AgendaItemDeleteBottomSheet(
                onDelete = {onAction(AgendaDetailAction.OnDeleteClick)},
                onToggleDeleteBottomSheet = {onAction(AgendaDetailAction.OnToggleDeleteBottomSheet)},
                isDeleteButtonLoading = state.isDeleteButtonLoading
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskReminderDetailsContent(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
    datePickerState: DatePickerState
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.at),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = "${state.selectedDateMillis.toLocalDateTime().hour}:${((state.selectedDateMillis.toLocalDateTime().minute)).toString().padStart(2, '0')}",
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetTimeClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = state.selectedDateMillis.toLocalDateTime().toDatePickerString(),
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetDateClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        // Notification Timer
        AgendaItemDetailNotificationDropdown(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onAction = onAction,
            state = state,
            isEditMode = isEditMode
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )

        if (state.isTimePickerDialogVisible) {
            AgendaItemDetailTimePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmTimeSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnDismissTimePickerDialog) },
                initialTime = state.time.time
            )
        }
        if (state.isDatePickerDialogVisible){
            AgendaDetailDatePicker(
                onAction = onAction,
                datePickerState = datePickerState
            )
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventDetailsContent(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
    datePickerState: DatePickerState
) {
    val numberOfPhotosAlreadySelected = state.localPhotos.size

    // MaxItems must always be bigger than 1 or the app crashes when the screen is recomposes after
    // the PhotoPicker is closed
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if(10 - numberOfPhotosAlreadySelected < 2) 2 else 10 - numberOfPhotosAlreadySelected
        )
        ) { uris ->
            if(uris.isNotEmpty()) {
                onAction(AgendaDetailAction.OnAddLocalPhotos(uris))
            }
        }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val uriList = listOf(uri)
                onAction(AgendaDetailAction.OnAddLocalPhotos(uriList))
            }
        }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        AgendaItemDetailPhotoPicker(
            photos = state.localPhotos.takeLast(10),
            onAddPhotosClick = {
                if (numberOfPhotosAlreadySelected < 9) {
                    pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            },
            onAction = onAction,
            isOnline = true,
            canAddPhotos = isEditMode
        )

        // From DateTime Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.from),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = "${state.selectedDateMillis.toLocalDateTime().hour}:${((state.selectedDateMillis.toLocalDateTime().minute)).toString().padStart(2, '0')}",
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetTimeClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = state.selectedDateMillis.toLocalDateTime().toDatePickerString(),
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetDateClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        // TO DateTime Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.to),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = "${state.selectedDateMillis.toLocalDateTime().hour}:${((state.selectedDateMillis.toLocalDateTime().minute)).toString().padStart(2, '0')}",
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetTimeClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = state.selectedDateMillis.toLocalDateTime().toDatePickerString(),
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnSetDateClick) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        // Notification Timer
        AgendaItemDetailNotificationDropdown(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onAction = onAction,
            state = state,
            isEditMode = isEditMode
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        AttendeeSection(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp),
            onAction = onAction,
            state = state,
            attendees = state.attendees,
        )

        if (state.isTimePickerDialogVisible) {
            AgendaItemDetailTimePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmTimeSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnDismissTimePickerDialog) },
                initialTime = state.time.time
            )
        }
        if (state.isDatePickerDialogVisible){
            AgendaDetailDatePicker(
                onAction = onAction,
                datePickerState = datePickerState
            )
        }

    }
}


@Preview
@Composable
private fun AgendaDetailScreenPreview() {
    TaskyTheme {
        AgendaDetailScreen(
            state = AgendaDetailState(
                taskyItem = TaskyItem(
                    id = "1",
                    title = "Task 1 Title",
                    description = "Weekly plan\nRole distribution",
                    time = LocalDateTime(2023, 1, 1, 12, 0),
                    type = TaskyType.EVENT,
                    details = TaskyItemDetails.Task(
                        isDone = false
                    ),
                    remindAt = LocalDateTime(2023, 1, 1, 11, 30),
                    notificationType = NotificationType.THIRTY_MINUTES_BEFORE
                ),
                isEditMode = true,
            ),
            onAction = {}
        )

    }

}
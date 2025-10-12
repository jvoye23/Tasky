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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AddAttendeeBottomSheet
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaDetailDatePicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDeleteBottomSheet
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailNotificationDropdown
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailPhotoPicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailTimePicker
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AgendaItemDetailTopAppBar
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeFilterType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeListItem
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toDatePickerString
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toLocalDateTime
import com.jvoye.tasky.core.domain.model.AttendeeBase
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDateTimePicker
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Awaiting
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Chevron_Right
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Offline
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
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
    val groupedAttendees: Map<Boolean, List<AttendeeBase>>  = remember(state.attendees) {
        state.attendees.groupBy { it.isGoing }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                ItemTypeSection(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    state = state
                )
            }
            item {
                TitleSection(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 24.dp),
                    onAction = onAction,
                    state = state
                )
            }
            item {
                HorizontalItemDetailsDivider()
            }
            item {
                DescriptionSection(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 24.dp),
                    onAction = onAction,
                    state = state
                )
            }
            item {
                HorizontalItemDetailsDivider()
            }
            // Event Photo Picker
            item {
                if (state.taskyItem.type == TaskyType.EVENT) {
                    EventPhotoPickerSection(
                        isEditMode = state.isEditMode,
                        onAction = onAction,
                        state = state
                    )
                }
            }

            // DateTimePicker Rows
            when(state.taskyItem.type) {
                TaskyType.TASK, TaskyType.REMINDER -> {
                    item {
                        TaskReminderDateTimePickerSection(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 24.dp),
                            isEditMode = state.isEditMode,
                            onAction = onAction,
                            state = state
                        )
                    }
                }
                TaskyType.EVENT -> {
                    item {
                        EventDateTimePickerSection(
                            isEditMode = state.isEditMode,
                            onAction = onAction,
                            state = state
                        )
                    }
                }
            }
            item {
                HorizontalItemDetailsDivider()
            }
            item {
                AgendaItemDetailNotificationDropdown(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 24.dp),
                    onAction = onAction,
                    state = state
                )
            }
            item {
                HorizontalItemDetailsDivider()
            }
            if (state.taskyItem.type == TaskyType.EVENT) {
                item {
                    AttendeeSection(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp),
                        onAction = onAction,
                        state = state
                    )
                }
            }
            attendeeList(
                state = state,
                onAction = onAction,
                groupedAttendees = groupedAttendees
            )
        }
        DeleteButtonSection(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            onAction = onAction,
            state = state
        )

        if (state.isDeleteBottomSheetVisible) {
            AgendaItemDeleteBottomSheet(
                onDelete = {onAction(AgendaDetailAction.OnDeleteClick)},
                onToggleDeleteBottomSheet = {onAction(AgendaDetailAction.OnToggleDeleteBottomSheet)},
                isDeleteButtonLoading = state.isDeleteButtonLoading
            )
        }
        if (state.isTimePickerDialogVisible) {
            AgendaItemDetailTimePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmTimeSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnToggleTimePickerDialog) },
                initialTime = state.time.time
            )
        }
        if (state.isDatePickerDialogVisible) {
            AgendaDetailDatePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmDateSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnToggleDatePickerDialog) },
                datePickerState = datePickerState
            )
        }
        if (state.isToTimePickerDialogVisible) {
            AgendaItemDetailTimePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmToTimeSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnToggleToTimePickerDialog) },
                initialTime = state.toTime.time
            )
        }

        if (state.isToDatePickerDialogVisible){
            AgendaDetailDatePicker(
                onConfirm = { onAction(AgendaDetailAction.ConfirmToDateSelection(it)) },
                onDismiss = { onAction(AgendaDetailAction.OnToggleToDatePickerDialog) },
                datePickerState = datePickerState,
                datePickerTitle = stringResource(R.string.select_to_date)
            )

        }

        if (state.isAddAttendeeBottomSheetVisible) {
            AddAttendeeBottomSheet(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun HorizontalItemDetailsDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.surfaceHigher
    )
}

@Composable
private fun ItemTypeSection (
    modifier: Modifier = Modifier,
    state: AgendaDetailState,
) {
    Row(
        modifier = modifier,
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
}

@Composable
private fun TitleSection (
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
}

@Composable
private fun DescriptionSection (
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
}

@Composable
private fun TaskReminderDateTimePickerSection(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
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
                onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleTimePickerDialog) },
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
                onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleTimePickerDialog) },
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun EventDateTimePickerSection(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleTimePickerDialog) },
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
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleDatePickerDialog) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceHigher
        )
        // To DateTime Selector Row
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
                    text = "${state.selectedToDateMillis.toLocalDateTime().hour}:${((state.selectedToDateMillis.toLocalDateTime().minute)).toString().padStart(2, '0')}",
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleToTimePickerDialog) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                TaskyDateTimePicker(
                    textModifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    isEditMode = state.isEditMode,
                    text = state.selectedToDateMillis.toLocalDateTime().toDatePickerString(),
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { if (isEditMode) onAction(AgendaDetailAction.OnToggleToDatePickerDialog) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun EventPhotoPickerSection (
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
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
    AgendaItemDetailPhotoPicker(
        modifier = modifier,
        //photos = state.localPhotos.takeLast(10),
        photos = state.localPhotosInfo.map { it.filePath },
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
}

@Composable
private fun DeleteButtonSection (
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAction(AgendaDetailAction.OnToggleDeleteBottomSheet) }                ,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceHigher
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
}

@Composable
private fun AttendeeSection(
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.visitors),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            if (!state.isOnline) {
                Icon(
                    imageVector = Icon_Offline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if(state.isEditMode) {
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceHigher)
                        .size(32.dp),

                    onClick = {
                        onAction(AgendaDetailAction.OnToggleAddAttendeeBottomSheet)
                    }
                ) {
                    Icon(
                        imageVector = Icon_plus,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AttendeeFilterType.entries.forEach { filterType ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = state.attendeeFilter == filterType,
                    onClick = { onAction(AgendaDetailAction.OnChangeAttendeeFilter(filterType)) },
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = filterType.label.asString(),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = null,
                    shape = RoundedCornerShape(100.dp)
                )
            }
        }
    }
    
}

private fun LazyListScope.attendeeList(
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
    groupedAttendees: Map<Boolean, List<AttendeeBase>>
) {
    if(state.taskyItem.type != TaskyType.EVENT) return

    when (state.attendeeFilter) {
        AttendeeFilterType.ALL -> {
            groupedAttendees.forEach { (isGoing, attendees) ->
                stickyHeader {
                    val title = if (isGoing) stringResource(R.string.going) else stringResource(R.string.not_going)
                    AttendeeListHeader(title = title)
                }
                items(attendees) { attendee ->
                    AttendeeListItem(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 4.dp),
                        onAction = onAction,
                        state = state,
                        attendeeBase = attendee
                    )
                }
            }
        }

        AttendeeFilterType.GOING -> {
            val goingAttendees = state.attendees.filter { it.isGoing }
            if (goingAttendees.isNotEmpty()) {
                stickyHeader {
                    AttendeeListHeader(title = stringResource(R.string.going))
                }
                items(goingAttendees) { attendee ->
                    AttendeeListItem(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 4.dp),
                        onAction = onAction,
                        state = state,
                        attendeeBase = attendee
                    )
                }
            }
        }

        AttendeeFilterType.NOT_GOING -> {
            val notGoingAttendees = state.attendees.filter { !it.isGoing }
            if (notGoingAttendees.isNotEmpty()) {
                stickyHeader {
                    AttendeeListHeader(title = stringResource(R.string.not_going))
                }
                items(notGoingAttendees) { attendee ->
                    AttendeeListItem(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 4.dp),
                        onAction = onAction,
                        state = state,
                        attendeeBase = attendee
                    )
                }
            }
        }
    }
}
@Composable
private fun AttendeeListHeader(title: String) {
    Text(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .padding(horizontal = 16.dp),
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
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
                isEditMode = false,
                attendees = attendeeListPreview
            ),
            onAction = {}
        )
    }
}

private val attendeeListPreview = listOf(
    EventAttendee(
        username = "Visitor One",
        email = "visitorOne@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = true,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0),
    ),
    EventAttendee(
        username = "Visitor Two",
        email = "visitorTwo@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = false,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0),
    ),
)
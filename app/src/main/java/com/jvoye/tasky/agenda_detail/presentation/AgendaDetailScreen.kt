package com.jvoye.tasky.agenda_detail.presentation

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.presentation.components.AgendaItemDetailTopAppBar
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDateTimePicker
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Awaiting
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bell
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Chevron_Right
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Dropdown
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun AgendaDetailScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AgendaDetailScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaDetailScreen(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun AgendaDetailScreen(
    state: AgendaDetailState
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            AgendaItemDetailTopAppBar(
                isEditMode = state.isEdit,
                taskyItem = state.taskyItem
            )
        }
    ) { innerPadding ->
        AgendaDetailScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .offset(x = 4.dp),
            state = state
        )
    }
}

@Composable
private fun AgendaDetailScreenContent(
    modifier: Modifier = Modifier,
    state: AgendaDetailState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = state.taskyItem?.type.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

        }
        Spacer(modifier = Modifier.height(28.dp))
        TaskContent(
            isEditMode = state.isEdit,
            state = state
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskContent(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    state: AgendaDetailState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {/*TODO()*/ },
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
                    text = if (state.taskyItem?.title == null) stringResource(R.string.title) else state.taskyItem.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icon_Chevron_Right,
                    contentDescription = null,
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary
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
                    .clickable {/*TODO()*/ },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = if(state.taskyItem?.description == null) stringResource(R.string.description) else  state.taskyItem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icon_Chevron_Right,
                    contentDescription = null,
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary
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
                    .padding(vertical = 24.dp),
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
                        isEditMode = state.isEdit,
                        text = "08:00",
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
                        contentColor = MaterialTheme.colorScheme.primary,
                        onClick = {/*TODO()*/},
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TaskyDateTimePicker(
                        textModifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp)),
                        isEditMode = state.isEdit,
                        text = "Jul 21, 2022",
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
                        contentColor = MaterialTheme.colorScheme.primary,
                        onClick = {/*TODO()*/},
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .clickable{ TODO() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceHigher,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(

                        imageVector = Icon_Bell,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "30 minutes before",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icon_Dropdown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surfaceHigher)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
                .clickable { /*TODO()*/ }
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
                text = stringResource(R.string.delete_task),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
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
                    id = 1,
                    title = "Task 1 Title",
                    description = "Weekly plan\nRole distribution",
                    time = LocalDateTime(2023, 1, 1, 12, 0),
                    type = TaskyType.TASK,
                    details = TaskyItemDetails.Task(
                        isDone = false
                    )
                ),
                isEdit = true
            )
        )

    }

}
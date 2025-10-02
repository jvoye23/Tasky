@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_list.components.AgendaDatePicker
import com.jvoye.tasky.agenda.presentation.agenda_list.components.AgendaFab
import com.jvoye.tasky.agenda.presentation.agenda_list.components.AgendaItemCard
import com.jvoye.tasky.agenda.presentation.agenda_list.components.AgendaTopBar
import com.jvoye.tasky.agenda.presentation.agenda_list.components.ScrollableDateRow
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime


@Composable
fun AgendaScreenRoot(
    onSuccessfulLogout: () -> Unit,
    onFabMenuItemClick: (Boolean, TaskyType) -> Unit,
    onAgendaItemClick: (Boolean, TaskyType, String) -> Unit,
    onAgendaItemMenuClick: (Boolean, TaskyType, String) -> Unit,
    viewModel: AgendaViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is AgendaEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            AgendaEvent.LogoutSuccess -> {
                Toast.makeText(
                    context,
                    R.string.logout_sucessful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulLogout()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AgendaScreen(
            state = state,
            onAction = { action ->
                when(action) {
                    is AgendaAction.OnFabMenuItemClick -> onFabMenuItemClick(action.isEditMode, action.taskyType)
                    is AgendaAction.OnAgendaItemClick -> onAgendaItemClick(action.isEditMode, action.taskyType, action.taskyItemId)
                    is AgendaAction.OnAgendaItemMenuClick -> onAgendaItemMenuClick(action.isEditMode, action.taskyType, action.taskyItemId)
                    else -> Unit
                }
                viewModel.onAction(action)

            }
        )
        if(state.isLoggingOut) {
            FullScreenLoadingIndicator()
        }
    }
}

@Composable
private fun AgendaScreen(
    state: AgendaState,
    onAction: (AgendaAction) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.selectedDateMillis
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            AgendaTopBar(
                state = state,
                action = onAction
            )
        },
        floatingActionButton = {
            AgendaFab(
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .offset(x = 4.dp),
                action = onAction,
                state = state
            )
        }
    ) { innerPadding ->
        AgendaScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .alpha(1f),
            state = state,
            action = onAction,
            datePickerState = datePickerState
        )
    }
}



@Composable
private fun AgendaScreenContent(
    modifier: Modifier = Modifier,
    state: AgendaState,
    action: (AgendaAction) -> Unit,
    datePickerState: DatePickerState
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
            .padding(top = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ScrollableDateRow(
            currentDate = state.currentDate,
            entries = state.dateRowEntries,
            action = action
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .padding(horizontal = 16.dp),
            text = state.dateHeadline,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.agendaList,
                key = { taskyItem -> taskyItem.id },
                contentType = {it.type }
            ) { taskyItem ->
                Row(modifier = Modifier.animateItem()) {
                    AgendaItemCard(
                        taskyItem = taskyItem,
                        action = action
                    )
                }
            }
        }
        if (state.isDatePickerDialogVisible){
            AgendaDatePicker(
                action = action,
                datePickerState = datePickerState
            )
        }
    }
}

@Composable
fun FullScreenLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                // Intercept all touch events to disable the underlying content
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}



@Preview(device = Devices.PIXEL_6_PRO, showSystemUi = true)
@Composable
fun AgendaScreenPreview() {
    TaskyTheme {
        AgendaScreen(
            state = AgendaState(),
            onAction = {}
        )
    }
}

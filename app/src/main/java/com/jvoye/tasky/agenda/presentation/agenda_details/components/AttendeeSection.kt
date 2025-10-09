package com.jvoye.tasky.agenda.presentation.agenda_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailAction
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailState
import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bin
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Offline
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.headlineXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.labelXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.mappers.getInitials
import kotlinx.datetime.LocalDateTime

enum class AttendeeFilterType { ALL, GOING, NOT_GOING }

@Composable
fun AttendeeSection(
    modifier: Modifier = Modifier,
    attendees: List<Attendee>,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
) {
    val groupedAttendees = remember(attendees) {
        attendees.groupBy { it.isGoing }
    }

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
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = state.attendeeFilter == AttendeeFilterType.ALL,
                onClick = { onAction(AgendaDetailAction.OnChangeAttendeeFilter(AttendeeFilterType.ALL)) },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.all),
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
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = state.attendeeFilter == AttendeeFilterType.GOING,
                onClick = { onAction(AgendaDetailAction.OnChangeAttendeeFilter(AttendeeFilterType.GOING))  },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.going),
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
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = state.attendeeFilter == AttendeeFilterType.NOT_GOING,
                onClick = { onAction(AgendaDetailAction.OnChangeAttendeeFilter(AttendeeFilterType.NOT_GOING))  },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.not_going),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            when (state.attendeeFilter) {
                AttendeeFilterType.ALL -> {
                    groupedAttendees.forEach { (isGoing, attendees) ->
                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 12.dp),
                                text = if (isGoing) stringResource(R.string.going) else stringResource(R.string.not_going),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        items(attendees) { attendee ->
                            AttendeeListItem(
                                onAction = onAction,
                                state = state,
                                attendee = attendee
                            )
                        }
                    }
                }

                AttendeeFilterType.GOING -> {
                    val goingAttendees = attendees.filter { it.isGoing }
                    if (goingAttendees.isNotEmpty()) {
                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 12.dp),
                                text = stringResource(R.string.going),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        items(goingAttendees) { attendee ->
                            AttendeeListItem(
                                onAction = onAction,
                                state = state,
                                attendee = attendee
                            )
                        }
                    }
                }

                AttendeeFilterType.NOT_GOING -> {
                    val notGoingAttendees = attendees.filter { !it.isGoing }
                    if (notGoingAttendees.isNotEmpty()) {
                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 12.dp),
                                text = stringResource(R.string.not_going),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        items(notGoingAttendees) { attendee ->
                            AttendeeListItem(
                                onAction = onAction,
                                state = state,
                                attendee = attendee
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendeeListItem (
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
    attendee: Attendee
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceHigher,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp)
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(attendee.username),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = attendee.username,
            style = MaterialTheme.typography.headlineXSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        if (state.isUserEventCreator) {
            Text(
                text = stringResource(R.string.creator),
                style = MaterialTheme.typography.labelXSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        if(state.isEditMode && !state.isUserEventCreator) {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = { onAction(AgendaDetailAction.OnDeleteAttendee(attendee)) }
            ) {
                Icon(
                    imageVector = Icon_Bin,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}




@Preview
@Composable
private fun AttendeeSectionPreview() {
    TaskyTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AttendeeSection(
                attendees = attendeeListPreview,
                onAction = {},
                state = AgendaDetailState(
                    isUserEventCreator = true,
                    isEditMode = true
                )
            )
        }
    }
}

private val attendeeListPreview = listOf(
    Attendee(
        username = "Visitor One",
        email = "visitorOne@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = true,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    ),
    Attendee(
        username = "Visitor Two",
        email = "visitorTwo@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = true,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    ),
    Attendee(
        username = "Visitor Three",
        email = "visitorThree@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = false,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    ),
    Attendee(
        username = "Visitor Four",
        email = "visitorFour@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = false,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    ),
    Attendee(
        username = "Visitor Five",
        email = "visitorFive@testmail.com",
        userId = "12345",
        eventId = "1234abc",
        isGoing = true,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    ),
)


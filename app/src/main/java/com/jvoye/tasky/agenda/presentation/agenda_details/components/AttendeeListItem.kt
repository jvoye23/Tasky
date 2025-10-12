package com.jvoye.tasky.agenda.presentation.agenda_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailAction
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailState
import com.jvoye.tasky.core.domain.model.AttendeeBase
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bin
import com.jvoye.tasky.core.presentation.designsystem.theme.headlineXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.labelXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.mappers.getInitials

@Composable
fun AttendeeListItem (
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
    attendeeBase: AttendeeBase
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
                text = getInitials(attendeeBase.username),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = attendeeBase.username,
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
                onClick = { onAction(AgendaDetailAction.OnDeleteAttendee(attendeeBase)) }
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

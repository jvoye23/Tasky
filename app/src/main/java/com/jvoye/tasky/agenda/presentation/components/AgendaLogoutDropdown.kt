package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun AgendaLogoutDropdown(
    expanded: Boolean,
    onLogOutClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        shape = RoundedCornerShape(size = 10.dp),
        shadowElevation = 0.dp,
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        containerColor = MaterialTheme.colorScheme.surfaceHigher,
        offset = DpOffset(x = 8.dp, y = 8.dp),
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 60.dp),
                    text = stringResource(R.string.log_out),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onLogOutClick()
            }
        )
    }
}



@Preview (showSystemUi = true, showBackground = true)
@Composable
private fun AgendaLogoutDropdownPreview () {
    TaskyTheme {
        AgendaLogoutDropdown(
            expanded = true,
            onDismissRequest = {},
            onLogOutClick = {}
        )
    }

}
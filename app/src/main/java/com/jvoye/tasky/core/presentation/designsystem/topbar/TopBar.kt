package com.jvoye.tasky.core.presentation.designsystem.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Edit
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigationIcon: @Composable () -> Unit,
    title: String,
    actionIcon: @Composable () -> Unit,
    isEditMode: Boolean

) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "01 MARCH 2022",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = {
                    //action(AgendaAction.OnCalendarIconClick)
                },

                content = {
                    Icon(
                        imageVector = Icon_X,
                        contentDescription = null
                    )
                }
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = {
                    //action(AgendaAction.OnCalendarIconClick)
                },

                content = {
                    Icon(
                        imageVector = Icon_Edit,
                        contentDescription = null
                    )
                }
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,

            )
    )
}
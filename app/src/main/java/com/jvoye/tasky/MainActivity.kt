package com.jvoye.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDeleteButton
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFilledButton
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFloatingActionButton
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyOutlinedButton
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskyThemeTest(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}


// Composable for testing the Material Theme setup
@Composable
private fun TaskyThemeTest(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskyOutlinedButton(
                    text = stringResource(R.string.cancel),
                    onClick = {},
                    modifier = Modifier
                        .weight(0.5f),
                )

                Spacer(modifier = Modifier.width(16.dp))

                TaskyDeleteButton(
                    text = stringResource(R.string.delete),
                    onClick = {},
                    isLoading = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(0.5f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskyFilledButton(
                    text = stringResource(R.string.get_started),
                    onClick = {},

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskyFloatingActionButton(
                    onClick = {}
                )
            }

        }
    }

}

package com.jvoye.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Calendar_Today
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.link
import com.jvoye.tasky.core.presentation.designsystem.theme.success

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
fun TaskyThemeTest(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.success)
            ) {
                Icon(
                    imageVector = Icon_Calendar_Today,
                    contentDescription = null
                )

                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge

                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = "Body Medium",
                    style = MaterialTheme.typography.bodyMedium

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.link)
            ) {
                Text(
                    text = "Body Small",
                    style = MaterialTheme.typography.bodySmall

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = "Label Medium",
                    style = MaterialTheme.typography.labelMedium

                )
            }

        }
    }

}

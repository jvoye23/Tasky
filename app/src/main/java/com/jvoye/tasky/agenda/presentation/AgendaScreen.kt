package com.jvoye.tasky.agenda.presentation

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AgendaScreenRoot(
    modifier: Modifier = Modifier
) {
    AgendaScreen()
}

@Composable
private fun AgendaScreen(
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Agenda Screen",
                style = MaterialTheme.typography.headlineLarge
            )

        }

    }

}

@Preview(device = Devices.PIXEL_6_PRO, showSystemUi = true)
@Composable
fun AgendaScreenPreview(modifier: Modifier = Modifier) {
    TaskyTheme {
        AgendaScreen()
    }
}
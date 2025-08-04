package com.jvoye.tasky.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier
) {
    LoginScreen()
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = " Welcome to Login Screen",
            style = MaterialTheme.typography.headlineLarge
        )
    }

}

@Preview(device = Devices.PIXEL_6_PRO, showSystemUi = true)
@Composable
fun LoginScreenPreview(
    modifier: Modifier = Modifier
) {
    TaskyTheme {
        LoginScreen()
    }

}


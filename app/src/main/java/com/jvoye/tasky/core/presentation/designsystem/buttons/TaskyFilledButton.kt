package com.jvoye.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    enabled: Boolean = true

) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary

        ),
        shape = RoundedCornerShape(38.dp),
        modifier = modifier,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .alpha(if (isLoading) 0f else 1f)
            )
        }
    }
}

@Preview (showSystemUi = true)
@Composable
private fun TaskyFilledButtonPreview(
) {
    TaskyTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TaskyFilledButton(
                text = "GET STARTED",
                onClick = {},
                isLoading = false,
                enabled = false
            )
        }
    }
}
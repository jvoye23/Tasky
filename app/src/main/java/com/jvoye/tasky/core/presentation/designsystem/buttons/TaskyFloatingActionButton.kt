package com.jvoye.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(68.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable{
                onClick
            },
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = Icon_plus,
            contentDescription = stringResource(R.string.plus_icon),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(24.dp)
        )
    }

}

@Preview (showSystemUi = true)
@Composable
private fun TaskyFloatingActionButtonPreview() {
    TaskyTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TaskyFloatingActionButton(
                onClick = {}
            )
        }
    }
}
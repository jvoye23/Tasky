package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Dropdown
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AgendaMonthTextButton(
    monthText: String,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 2.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = monthText.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icon_Dropdown,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun AgendaMonthTextButtonPreview() {
    TaskyTheme {
        AgendaMonthTextButton(
            monthText = "August",
            onClick = {}
        )
    }
}
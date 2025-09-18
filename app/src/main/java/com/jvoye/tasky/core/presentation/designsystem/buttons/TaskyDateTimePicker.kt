package com.jvoye.tasky.core.presentation.designsystem.buttons


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Dropdown
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyDateTimePicker(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    isEditMode: Boolean = true,
    text: String,
    textStyle: TextStyle,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Row (
        modifier = modifier
            .background(containerColor)
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = textModifier,
            text = text,
            style = textStyle,
            color = contentColor
        )
        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icon_Dropdown,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = if (isEditMode) contentColor else containerColor.copy(alpha = 0f)
        )
        
    }
}

@Preview
@Composable
private fun AgendaMonthTextButtonPreview() {
    TaskyTheme {
        TaskyDateTimePicker(
            text = "August",
            onClick = {},
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier,
            isEditMode = true,
            textStyle = MaterialTheme.typography.labelMedium,
            textModifier = Modifier
        )
    }
}
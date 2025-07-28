package com.jvoye.tasky.core.presentation.designsystem.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.success
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun TaskyTextField(
    state: TextFieldState,
    endIcon: ImageVector?,
    borderColor: Color,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    BasicTextField(
        state = state,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                MaterialTheme.colorScheme.surfaceHigher
            )
            .border(
                width = 1.dp,
                color = if (isFocused) {
                    MaterialTheme.colorScheme.outline
                } else {
                    borderColor
                },
                shape = RoundedCornerShape(10.dp)
            )
            .padding(20.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.7f
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))

                if (endIcon != null){
                    Icon(
                        imageVector = endIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.success
                    )
                }
            }
            innerBox()
        }
    )
}

@Preview (showSystemUi = true)
@Composable
private fun TaskyTextFieldPreview() {
    TaskyTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskyTextField(
                state = rememberTextFieldState(),
                endIcon = Icon_Check,
                hint = "email@test.com",
                borderColor = MaterialTheme.colorScheme.surfaceHigher,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
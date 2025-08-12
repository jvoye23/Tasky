package com.jvoye.tasky.core.presentation.designsystem.util

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow

fun TextFieldState.textAsFlow() = snapshotFlow { text }



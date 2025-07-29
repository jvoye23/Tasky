package com.jvoye.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jvoye.tasky.auth.presentation.register.RegisterScreenRoot
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskyTheme {
                RegisterScreenRoot()
            }
        }
    }
}
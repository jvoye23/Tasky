package com.jvoye.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.core.util.deleteAllCompressedFiles
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.navigation.NavigationRoot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isCheckingAuth
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0)
        )
        setContent {

            val state = viewModel.state.collectAsStateWithLifecycle()

            TaskyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (!state.value.isCheckingAuth){
                        NavigationRoot(isLoggedIn = state.value.isLoggedIn)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        GlobalScope.launch {
            deleteAllCompressedFiles(application)
        }
    }
}
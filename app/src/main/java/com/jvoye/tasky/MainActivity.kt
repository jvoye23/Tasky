package com.jvoye.tasky

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.success
import com.jvoye.tasky.navigation.NavigationRoot
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

        handleIntent(intent)

        setContent {

            val state = viewModel.state.collectAsStateWithLifecycle()

            TaskyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (!state.value.isCheckingAuth){
                        NavigationRoot(
                            isLoggedIn = state.value.isLoggedIn,
                            pendingNavigation = state.value.pendingNavigation,
                            onNavigationHandled = viewModel::onNavigationHandled
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == "NAVIGATE_TO_AGENDA_DETAIL") {
            val itemId = intent.getStringExtra("TASKY_ITEM_ID")
            val itemType = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("TASKY_TYPE") as? TaskyType
            } else {
                intent.getSerializableExtra("TASKY_TYPE", TaskyType::class.java)
            }

            if (itemId != null && itemType != null) {
                viewModel.triggerNavigation(itemId, itemType)
            }
        }
    }
}
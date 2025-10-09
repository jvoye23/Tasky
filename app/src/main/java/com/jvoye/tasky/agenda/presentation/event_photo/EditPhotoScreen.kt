package com.jvoye.tasky.agenda.presentation.event_photo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.presentation.event_photo.components.EditPhotoTopAppBar
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditPhotoScreenRoot(
    onCloseAndCancelClick: () -> Unit,
    viewModel: EditPhotoScreenViewModel = koinViewModel ()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when(event){
             EditPhotoEvent.PhotoDeleted -> {
                Toast.makeText(
                    context,
                    R.string.photo_deleted,
                    Toast.LENGTH_LONG
                ).show()
                onCloseAndCancelClick()
            }
            is EditPhotoEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
                onCloseAndCancelClick()
            }

        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    EditPhotoScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is EditPhotoAction.OnCloseAndCancelClick -> onCloseAndCancelClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )



}

@Composable
fun EditPhotoScreen(
    state: EditPhotoState,
    onAction: (EditPhotoAction) -> Unit
    ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            EditPhotoTopAppBar(
                onAction = onAction,
                state = state,
                title = state.titleText
            )
            
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = state.photoUrl ?: state.localPhotoPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()

                    .padding(vertical = 40.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(5.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 20.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.error_couldnt_load_image),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }

    }
    
}

@Preview
@Composable
private fun EditPhotoScreenPreview() {
    TaskyTheme {
        EditPhotoScreen(
            state = EditPhotoState(
                isEditMode = true,
                isOnline = true,
                titleText = "01 MARCH 2022"
            ),
            onAction = {}
        )
    }
}
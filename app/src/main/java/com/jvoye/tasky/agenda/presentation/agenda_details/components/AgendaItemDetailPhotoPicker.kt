package com.jvoye.tasky.agenda.presentation.agenda_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Offline
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun AgendaItemDetailPhotoPicker(
    modifier: Modifier = Modifier,
    photos: List<String> = emptyList(),
    onAddPhotosClick: () -> Unit,
    isOnline: Boolean,
    isEditMode: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surfaceHigher)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        if (photos.isEmpty()) {
            EmptyPhotoGrid(
                modifier = Modifier.align(Alignment.Center),
                onClick = onAddPhotosClick
            )
        } else {
            Column {
                PhotoSectionHeader(isOnline = isOnline)
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Display a box for each photo.
                    photos.forEach { photoUri ->
                        PhotoBox(
                            // weight(1f) makes each item in a row take up equal space.
                            //modifier = Modifier.size(65.dp),
                            modifier = Modifier.weight(1f),
                            onAddPhotosClick = onAddPhotosClick,
                            localPhotoPath = photoUri

                        )
                    }

                    val remainingSlots = if (photos.size < 5) 5 - photos.size else
                        10 - photos.size
                    if (remainingSlots > 0) {
                        // If in edit mode, the first empty slot becomes an "add" button.
                        if (isEditMode && isOnline) {
                            PhotoBox(
                                modifier = Modifier.weight(1f),
                                onAddPhotosClick = onAddPhotosClick,
                                localPhotoPath = "" // Empty URI signifies the "add" button
                            )
                            // Fills the rest with invisible spacers to maintain alignment.
                            repeat(remainingSlots - 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        } else {
                            // Otherwise, fill all remaining slots with spacers.
                            repeat(remainingSlots) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoBox(
    modifier: Modifier = Modifier,
    onAddPhotosClick: () -> Unit,
    localPhotoPath: String
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onAddPhotosClick() }
            .border(
                width = 2.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .size(65.dp),
        contentAlignment = Alignment.Center
    ) {
        if (localPhotoPath.isNotBlank()) {
            SubcomposeAsyncImage(
                model = localPhotoPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(5.dp))
                    .padding(2.dp),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 2.dp,
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
        } else {
            // This is the "add photo" slot.
            Icon(
                imageVector = Icon_plus,
                contentDescription = stringResource(R.string.add_photo),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun EmptyPhotoGrid(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icon_plus,
            contentDescription = stringResource(R.string.add_photo),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.add_photos),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun PhotoSectionHeader(
    isOnline: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.photos),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (!isOnline) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icon_Offline,
                contentDescription = stringResource(R.string.offline_indicator),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview
@Composable
private fun AgendaItemDetailPhotoPickerPreview() {
    TaskyTheme {
        AgendaItemDetailPhotoPicker(
            photos = listOf(
                //"1", "2", "3", "4", "5", "6", "7", "8", "9"
                "1", "2", "3", "4", "5", "6"
                //"1", "2", "3", "4"
                //"1"
            ),
            onAddPhotosClick = {},
            isOnline = true,
            isEditMode = true
        )
    }
}
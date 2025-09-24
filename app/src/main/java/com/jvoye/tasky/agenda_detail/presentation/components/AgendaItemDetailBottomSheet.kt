package com.jvoye.tasky.agenda_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailAction
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaItemDetailBottomSheet(
    modifier: Modifier = Modifier,
    //action: AgendaDetailAction
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { },
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        
    }
    
    
}

@Preview
@Composable
private fun AgendaItemDetailBottomSheetPreview() {
    TaskyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AgendaItemDetailBottomSheet(

               
            )
        }
    }
}
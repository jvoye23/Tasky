package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AgendaDetailScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AgendaDetailScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaDetailScreen(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgendaDetailScreen(
    state: AgendaDetailState
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {

        }
    ) { innerPadding ->
        AgendaDetailScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .offset(x = 4.dp),
            state = state
        )
    }
}

@Composable
private fun AgendaDetailScreenContent(
    modifier: Modifier = Modifier,
    state: AgendaDetailState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.agendaTypeName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}




@Preview
@Composable
private fun AgendaDetailScreenPreview() {
    TaskyTheme {
        AgendaDetailScreen(
            state = AgendaDetailState(
                agendaTypeName = "Agenda Type Name"
            )
        )

    }

}
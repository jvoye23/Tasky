package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.agenda.presentation.AgendaAction
import com.jvoye.tasky.agenda.presentation.util.DateRowEntry
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.labelXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.supplementary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun ScrollableDateRow(
    entries: List<DateRowEntry>?,
    currentDate: LocalDate?,
    action: (AgendaAction) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // The index of the item to be centered.
    val centerIndex = 15

    LaunchedEffect(Unit) {
        delay(50)
        lazyListState.animateScrollToItem(
            index = centerIndex,
            scrollOffset = -lazyListState.layoutInfo.viewportSize.width / 2
        )
    }
    LazyRow(
        state = lazyListState,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),

    ) {
        if (entries != null) {
            items(entries) { entry ->
                DateRowItem(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp)
                        .clickable {},
                    isSelected = currentDate == entry.localDate,
                    date = entry,
                    onItemClick = { action(AgendaAction.OnDateRowItemClick(entry.localDate)) }
                )
            }
        }
    }
}

@Composable
fun DateRowItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    date: DateRowEntry,
    onItemClick: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(min = 55.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.supplementary
                else MaterialTheme.colorScheme.surface
            )
            .padding(15.dp)
            .clickable {
                onItemClick(date.localDate)
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = date.dayOfWeek.toString().take(1),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSystemInDarkTheme() && isSelected) MaterialTheme.colorScheme.onPrimary
                    else if (isSelected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = date.dayOfTheMonth.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = if(isSystemInDarkTheme() && isSelected) MaterialTheme.colorScheme.onPrimary
                     else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)@Composable
private fun DateRowItemPreview() {
    TaskyTheme {
        DateRowItem(
            modifier = Modifier,
            isSelected = true,
            date = DateRowEntry(
                localDate = LocalDate(2023, 10, 10),
                dayOfTheMonth = 10,
                dayOfWeek = LocalDate(2023, 10, 10).dayOfWeek
            ),
            onItemClick = {}
        )

    }
    
}

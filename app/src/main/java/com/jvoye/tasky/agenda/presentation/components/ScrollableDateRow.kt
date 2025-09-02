package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.agenda.presentation.AgendaAction
import com.jvoye.tasky.agenda.presentation.util.DateRowEntry
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
        // Wait for a short duration to allow initial composition and measurement.
        delay(100)

        // A loop to keep retrying until the target item is found.
        var isCentered = false
        while (!isCentered) {
            val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.find { it.index == centerIndex }

            if (itemInfo != null) {
                // Calculation to center the item.
                val viewportWidth = lazyListState.layoutInfo.viewportSize.width
                val scrollOffset = itemInfo.offset - (viewportWidth / 2 - itemInfo.size / 2)

                coroutineScope.launch {
                    lazyListState.animateScrollBy(scrollOffset.toFloat())
                }
                isCentered = true
            } else {
                // If the item isn't visible yet, scroll to its general location to bring it into view.
                lazyListState.scrollToItem(index = centerIndex)
                delay(50) // Wait for a short moment before retrying.
            }
        }
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
            .height(70.dp)
            .width(40.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) MaterialTheme .colorScheme.supplementary
                else MaterialTheme.colorScheme.surface
            )
            .padding(vertical = 14.dp)
            .clickable {
                onItemClick(date.localDate)
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = date.dayOfWeek.toString().take(1),
            style = MaterialTheme.typography.labelXSmall,
            color = if (isSystemInDarkTheme() && isSelected) MaterialTheme.colorScheme.onPrimary
                    else if (isSelected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = date.dayOfTheMonth.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = if(isSystemInDarkTheme() && isSelected) MaterialTheme.colorScheme.onPrimary
                     else MaterialTheme.colorScheme.onSurface
        )
    }
}

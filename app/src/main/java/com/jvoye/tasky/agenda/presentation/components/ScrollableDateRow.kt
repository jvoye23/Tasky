package com.jvoye.tasky.agenda.presentation.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.labelXSmall
import com.jvoye.tasky.core.presentation.designsystem.theme.supplementary
import kotlinx.datetime.DayOfWeek

@Composable
fun ScrollableDateRow() {

    val itemsList = (1..31).toList()
    val daysOfTheWeek = listOf<String>("M", "T", "W", "T", "F", "S", "S")
    val newList = mapItemsToCyclicDays(itemsList, daysOfTheWeek)


    LazyRow(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)

    ) {
        items(newList) { pair ->
            DateRowItem(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 10.dp),
                dayOfWeek = pair.first.toString(),
                dayOfMonth = pair.second,
                isToday = true
            )
        }
    }
}

private fun mapItemsToCyclicDays(items: List<Int>, days: List<String>): List<Pair<Int, String>> {
    if (days.isEmpty()) {
        return emptyList() // Avoid division by zero if the days list is empty
    }
    return items.map { item ->
        // Pair each item with a day from the days list
        // The modulo operator (%) ensures the days list repeats
        item to days[item % days.size]
    }
}

@Composable
fun DateRowItem(
    modifier: Modifier = Modifier,
    dayOfWeek: String,
    dayOfMonth: String,
    isToday: Boolean
) {
    Column(
        modifier = modifier
            .height(70.dp)
            .width(40.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isToday) MaterialTheme .colorScheme.supplementary
                else MaterialTheme.colorScheme.surface
            )
            .padding(vertical = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = dayOfMonth,
            style = MaterialTheme.typography.labelXSmall,
            color = if (isToday) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )


    }
    
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ScrollableDateRowPreview() {
    TaskyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ScrollableDateRow()
        }
    }
}
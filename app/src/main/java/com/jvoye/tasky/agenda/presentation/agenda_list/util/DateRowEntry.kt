@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_list.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime


data class DateRowEntry(
    val localDate: LocalDate,
    val dayOfTheMonth: Int,
    val dayOfWeek: DayOfWeek
)
package com.jvoye.tasky.agenda_detail.presentation.mappers

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun getItemDetailDateString(localDateTime: LocalDateTime?): String {
    // 01 MARCH 2022
    return localDateTime?.format(
        LocalDateTime.Format {
            day()
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            year()
        }
    ) ?: ""
}

@OptIn(ExperimentalTime::class)
fun Long.toLocalDateTimeString(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val formattedDate =  instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return formattedDate.format(
        LocalDateTime.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            day()
            char(',')
            char(' ')
            year()
        }
    )
}

fun LocalDateTime.toDatePickerString(): String {
    return this.format(
        LocalDateTime.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            day()
            char(',')
            char(' ')
            year()
        }
    )
}
package com.jvoye.tasky.agenda.presentation.agenda_details.mappers

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
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

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toEpochMilliseconds(): Long {
    return this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

@OptIn(ExperimentalTime::class)
fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

@OptIn(ExperimentalTime::class)
fun convertIsoStringToSystemLocalDateTime(isoString: String): LocalDateTime {
    val instant = Instant.parse(isoString)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}

@OptIn(ExperimentalTime::class)
fun convertIsoStringToUtcDateTime(isoString: String): LocalDateTime {
    val instant = Instant.parse(isoString)
    return instant.toLocalDateTime(TimeZone.UTC)
}

@OptIn(ExperimentalTime::class)
fun getNextHalfMarkLocalTime(): LocalDateTime {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val newHour = if (now.hour == 23) 0 else now.hour + 1

    val newTime = if (now.minute < 30) {
        LocalDateTime(
            year = now.year,
            month = now.month,
            day = now.day,
            hour = now.hour,
            minute = 30
        )
    } else {
        LocalDateTime(
            year = now.year,
            month = now.month,
            day = now.day,
            hour = newHour,
            minute = 0
        )
    }
    return newTime
}

@OptIn(ExperimentalTime::class)
fun getNextHalfMarkInstant(): Instant {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val newHour = if (now.hour == 23) 0 else now.hour + 1

    val newTime = if (now.minute < 30) {
        LocalDateTime(
            year = now.year,
            month = now.month,
            day = now.day,
            hour = now.hour,
            minute = 30
        )
    } else {
        LocalDateTime(
            year = now.year,
            month = now.month,
            day = now.day,
            hour = newHour,
            minute = 0
        )
    }
    return newTime.toInstant(TimeZone.currentSystemDefault())







}

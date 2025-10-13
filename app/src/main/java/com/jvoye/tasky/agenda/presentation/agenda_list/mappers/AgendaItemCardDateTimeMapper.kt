package com.jvoye.tasky.agenda.presentation.agenda_list.mappers

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun getItemCardDateTimeString(localDateTime: LocalDateTime?): String {
    if (localDateTime == null) {
        return ""
    } else {
        // Mar 5, 10:00
        return localDateTime.format(
            LocalDateTime.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                day()
                char(',')
                char(' ')
                hour()
                char(':')
                minute()
            }
        )
    }
}




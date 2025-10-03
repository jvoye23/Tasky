package com.jvoye.tasky.agenda.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

enum class NotificationType(val offset: Duration) {
    TEN_MINUTES_BEFORE(10.minutes),
    THIRTY_MINUTES_BEFORE(30.minutes),
    ONE_HOUR_BEFORE(1.hours),
    SIX_HOURS_BEFORE(6.hours),
    ONE_DAY_BEFORE(1.days),


}
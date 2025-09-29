package com.jvoye.tasky.core.database.mappers

import androidx.room.TypeConverter
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.Attendee

class RoomConverters {
    @TypeConverter
    fun fromTaskyType(taskyType: TaskyType): String {
        return taskyType.name
    }

    @TypeConverter
    fun toTaskyType(value: String): TaskyType {
        return TaskyType.valueOf(value)
    }

    @TypeConverter
    fun fromAttendeeList(attendees: List<Attendee>): String {
        return attendees.joinToString(",") { "${it.username},${it.email}" }
    }

    @TypeConverter
    fun toAttendeeList(value: String): List<Attendee> {
        TODO()
    }
}
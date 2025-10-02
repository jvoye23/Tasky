package com.jvoye.tasky.core.database.mappers

import androidx.room.TypeConverter

import com.jvoye.tasky.core.domain.model.Attendee
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/*class RoomConverters {
    @TypeConverter
    fun fromTaskyType(taskyType: TaskyType): String {
        return taskyType.name
    }

    @TypeConverter
    fun toTaskyType(value: String): TaskyType {
        return TaskyType.valueOf(value)
    }*/

    /*@TypeConverter
    fun fromAttendees(attendees: List<Attendee>): String {
        return attendees.joinToString(",") {
            "${it.username},${it.email}"
        }
        //return Json.encodeToString(attendees)
    }

    @TypeConverter
    fun toAttendees(value: String): List<Attendee> {
        return value.split(",").map {
            val attendee = it.split(",")
            Attendee(
                username = "username",
                email = "email1",
                userId = "userId",
                eventId = "eventId",
                isGoing = false,
                remindAt = LocalDateTime(2023, 1, 1, 1, 1))

        } */
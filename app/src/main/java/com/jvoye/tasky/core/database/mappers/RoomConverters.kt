package com.jvoye.tasky.core.database.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jvoye.tasky.core.domain.model.AttendeeBase
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.RemotePhoto

class RoomConverters {
    private val gson = Gson()

    // Converts a List of photos into a JSON string
    @TypeConverter
    fun fromRemotePhotoListToJsonString(eventPhotos: List<RemotePhoto>?): String? {
        if (eventPhotos == null) {
            return null
        }
        return gson.toJson(eventPhotos)
    }

    @TypeConverter
    fun toRemotePhotoList(json: String?): List<RemotePhoto>? {
        if (json.isNullOrBlank()) {
            return null
        }
        val type = object : TypeToken<List<RemotePhoto>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromEventAttendeeListToJsonString(attendees: List<EventAttendee>?): String? {
        if (attendees == null) {
            return null
        }
        return gson.toJson(attendees)
    }

    @TypeConverter
    fun toEventAttendeeList(json: String?): List<EventAttendee>? {
        if (json.isNullOrBlank()) {
            return null
        }
        val type = object : TypeToken<List<EventAttendee>>() {}.type
        return gson.fromJson(json, type)
    }


}

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
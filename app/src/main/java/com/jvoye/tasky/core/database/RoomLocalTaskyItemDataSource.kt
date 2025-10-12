package com.jvoye.tasky.core.database

import android.database.sqlite.SQLiteFullException
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.database.dao.EventDao
import com.jvoye.tasky.core.database.dao.ReminderDao
import com.jvoye.tasky.core.database.dao.TaskDao
import com.jvoye.tasky.core.database.mappers.toEventEntity
import com.jvoye.tasky.core.database.mappers.toReminderEntity
import com.jvoye.tasky.core.database.mappers.toTaskEntity
import com.jvoye.tasky.core.database.mappers.toTaskyItem
import com.jvoye.tasky.core.domain.LocalTaskyItemDataSource
import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RoomLocalTaskyItemDataSource(
    private val taskDao: TaskDao,
    private val reminderDao: ReminderDao,
    private val eventDao: EventDao
): LocalTaskyItemDataSource {

    override fun getTaskyItems(): Flow<List<TaskyItem>> {
        val tasks = taskDao.getTasks()
            .map { taskEntities ->
                taskEntities.map { it.toTaskyItem() }
            }

        val reminders = reminderDao.getReminders()
            .map { reminderEntities ->
                reminderEntities.map { it.toTaskyItem() }
            }

        val events = eventDao.getEvents()
            .map { eventEntities ->
                eventEntities.map { it.toTaskyItem() }
            }

        return combine(tasks, reminders, events) { tasks, reminders, events ->
            (tasks + reminders + events).sortedBy { it.time }
        }

    }

    override suspend fun getTaskyItem(taskyType: TaskyType, taskyItemId: String): TaskyItem {
        return when(taskyType) {
            TaskyType.TASK -> {
                taskDao.getTask(taskyItemId)?.toTaskyItem() ?: throw Exception("Task not found")
            }

            TaskyType.REMINDER -> {
                reminderDao.getReminder(taskyItemId)?.toTaskyItem() ?: throw Exception("Reminder not found")
            }

            TaskyType.EVENT -> {
                eventDao.getEvent(taskyItemId)?.toTaskyItem() ?: throw Exception("Event not found")
            }
        }
    }


    override suspend fun upsertFullAgenda(fullAgenda: FullAgenda): Result<List<TaskyItemId>, DataError.Local> {
        return try {
            val taskEntities = fullAgenda.tasks.map { it.toTaskEntity() }
            val reminderEntities = fullAgenda.reminders.map { it.toReminderEntity() }
            val eventEntities = fullAgenda.events.map { it.toEventEntity() }

            taskDao.upsertTasks(taskEntities)
            reminderDao.upsertReminders(reminderEntities)
            eventDao.upsertEvents(eventEntities)

            Result.Success(fullAgenda.tasks.map { it.id } + fullAgenda.reminders.map { it.id } + fullAgenda.events.map { it.id })

        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertTaskyItem(taskyItem: TaskyItem): Result<TaskyItemId, DataError.Local> {
        return try {
            when(taskyItem.type) {
                TaskyType.TASK -> taskDao.upsertTask(taskyItem.toTaskEntity())
                TaskyType.REMINDER -> reminderDao.upsertReminder(taskyItem.toReminderEntity())
                TaskyType.EVENT -> eventDao.upsertEvent(taskyItem.toEventEntity())
            }
            Result.Success(taskyItem.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTaskyItem(type: TaskyType, taskyItemId: String) {
        when(type) {
            TaskyType.TASK -> taskDao.deleteTask(taskyItemId)
            TaskyType.REMINDER -> reminderDao.deleteReminder(taskyItemId)
            TaskyType.EVENT -> eventDao.deleteEvent(taskyItemId)
        }
    }

    override suspend fun deleteAllTaskyItems() {
        taskDao.deleteAllTasks()
        reminderDao.deleteAllReminders()
        eventDao.deleteAllEvents()
    }

}





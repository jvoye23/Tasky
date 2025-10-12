package com.jvoye.tasky.core.data.networking

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.data.networking.dto.EventDto
import com.jvoye.tasky.core.data.networking.dto.EventInfoDto
import com.jvoye.tasky.core.data.networking.dto.FullAgendaDto
import com.jvoye.tasky.core.data.networking.dto.ReminderDto
import com.jvoye.tasky.core.data.networking.dto.TaskDto
import com.jvoye.tasky.core.data.networking.mappers.toCreateEventRequest
import com.jvoye.tasky.core.data.networking.mappers.toCreateReminderRequest
import com.jvoye.tasky.core.data.networking.mappers.toCreateTaskRequest
import com.jvoye.tasky.core.data.networking.mappers.toFullAgenda
import com.jvoye.tasky.core.data.networking.mappers.toTaskyItem
import com.jvoye.tasky.core.data.networking.mappers.toUpdateEventRequest
import com.jvoye.tasky.core.domain.RemoteTaskyItemDataSource
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import timber.log.Timber


class KtorRemoteTaskyItemDataSource(
    private val httpClient: HttpClient
) : RemoteTaskyItemDataSource {
    override suspend fun getFullAgenda(): Result<FullAgenda, DataError.Network> {
        return httpClient.get<FullAgendaDto>(
            route = "/fullAgenda",
        ).map { fullAgendaDto ->
            fullAgendaDto.toFullAgenda()
        }
    }

    override suspend fun postTaskyItem(taskyItem: TaskyItem): Result<TaskyItem, DataError.Network> {
        return if (taskyItem.type == TaskyType.TASK) {
            httpClient.post<CreateTaskRequest, TaskDto>(
                route = "/task",
                body = taskyItem.toCreateTaskRequest()
            ).map {
                it.toTaskyItem()
            }
        } else {
            httpClient.post<CreateReminderRequest, ReminderDto>(
                route = "/reminder",
                body = taskyItem.toCreateReminderRequest()
            ).map {
                it.toTaskyItem()
            }
        }
    }

    override suspend fun postTaskyEvent(taskyItem: TaskyItem): Result<EventDto, DataError.Network> {
        return httpClient.post<CreateEventRequest, EventDto>(
            route = "/event",
            body = taskyItem.toCreateEventRequest()
        )
    }

    override suspend fun updateTaskyEvent(taskyItem: TaskyItem): Result<EventDto, DataError.Network> {
        return httpClient.put<UpdateEventRequest, EventDto>(
            route = "event/${taskyItem.id}",
            body = taskyItem.toUpdateEventRequest()
        )
    }

    override suspend fun updateTaskyItem(taskyItem: TaskyItem): Result<TaskyItem, DataError.Network> {
        when(taskyItem.type) {
            TaskyType.TASK -> {
                return httpClient.put<CreateTaskRequest, TaskDto>(
                    route = "/task",
                    body = taskyItem.toCreateTaskRequest()
                ).map {
                    it.toTaskyItem()
                }
            }
            TaskyType.REMINDER -> {
                return httpClient.put<CreateReminderRequest, ReminderDto>(
                    route = "/reminder",
                    body = taskyItem.toCreateReminderRequest()
                ).map {
                    it.toTaskyItem()
                }

            }
            TaskyType.EVENT -> {
                return httpClient.put<CreateEventRequest, EventDto>(
                    route = "/event/${taskyItem.id}",
                    body = taskyItem.toCreateEventRequest()
                ).map {
                    it.toTaskyItem()
                }
            }
        }
    }

    override suspend fun deleteTaskyItem(taskyItemId: String, taskyType: TaskyType): EmptyResult<DataError.Network> {
        when (taskyType) {
            TaskyType.TASK -> {
                return httpClient.delete(
                    route = "/task/$taskyItemId",
                )
            }
            TaskyType.REMINDER -> {
                return httpClient.delete(
                    route = "/reminder/$taskyItemId",
                )
            }
            TaskyType.EVENT -> {
                return httpClient.delete(
                    route = "/event",
                    queryParameters = mapOf("eventId" to taskyItemId)
                )
            }
        }
    }

    override suspend fun confirmPhotosUpload(taskyItemId: String, confirmUploadRequest: ConfirmUploadRequest
    ): Result<TaskyItem, DataError.Network> {
        return httpClient.post<ConfirmUploadRequest, EventInfoDto>(
            route = "/event/$taskyItemId/confirm-upload",
            body = confirmUploadRequest
        ).map { it.toTaskyItem() }
    }

    override suspend fun uploadPhoto(url: String,photo: ByteArray): EmptyResult<DataError.Network> {
        val amzHttpClient = HttpClient(CIO) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
        }


        return amzHttpClient.put<ByteArray, Unit>(
            route = url,
            body = photo,
            contentType = ContentType.Image.JPEG
        )
    }
}


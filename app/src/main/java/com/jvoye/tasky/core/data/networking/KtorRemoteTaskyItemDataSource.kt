package com.jvoye.tasky.core.data.networking

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.data.networking.mappers.toCreateReminderRequest
import com.jvoye.tasky.core.data.networking.mappers.toCreateTaskRequest
import com.jvoye.tasky.core.data.networking.mappers.toFullAgenda
import com.jvoye.tasky.core.data.networking.mappers.toTaskyItem
import com.jvoye.tasky.core.domain.RemoteTaskyItemDataSource
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse


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
        when (taskyItem.type) {
            TaskyType.TASK -> {
                val result = safeCall<TaskDto> {
                    httpClient.post<CreateTaskRequest, TaskDto>(
                        route = "/task",
                        body = taskyItem.toCreateTaskRequest()
                    ) as HttpResponse
                }
                return result.map {
                    it.toTaskyItem()
                }
            }
            TaskyType.REMINDER -> {
                val result = safeCall<ReminderDto> {
                    httpClient.post<CreateReminderRequest, ReminderDto>(
                        route = "/reminder",
                        body = taskyItem.toCreateReminderRequest()
                    ) as HttpResponse
                }
                return result.map {
                    it.toTaskyItem()
                }
            }
            TaskyType.EVENT -> {
                TODO("Not yet implemented")
            }
        }
    }

    override suspend fun updateTaskyItem(taskyItem: TaskyItem): Result<TaskyItem, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskyItem(taskyItemId: String, taskyType: TaskyType): EmptyResult<DataError.Network> {
        when (taskyType) {
            TaskyType.TASK -> {
                return httpClient.delete(
                    route = "/task",
                    queryParameters = mapOf(
                        "taskId" to taskyItemId
                    )
                )
            }
            TaskyType.REMINDER -> {
                return httpClient.delete(
                    route = "/reminder",
                    queryParameters = mapOf(
                        "reminderId" to taskyItemId
                    )
                )
            }
            TaskyType.EVENT -> {
                return httpClient.delete(
                    route = "/event",
                    queryParameters = mapOf(
                        "eventId" to taskyItemId
                    )
                )
            }
        }
    }
}


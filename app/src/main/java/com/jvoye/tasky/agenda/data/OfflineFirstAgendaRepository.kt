package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.data.networking.ConfirmUploadRequest
import com.jvoye.tasky.core.data.networking.mappers.toTaskyItem
import com.jvoye.tasky.core.domain.LocalTaskyItemDataSource
import com.jvoye.tasky.core.domain.RemoteTaskyItemDataSource
import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.detailsAsEvent
import com.jvoye.tasky.core.domain.model.toAgendaNotification
import com.jvoye.tasky.core.domain.notification.NotificationService
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.Result.*
import com.jvoye.tasky.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class OfflineFirstAgendaRepository(
    private val localTaskyItemDataSource: LocalTaskyItemDataSource,
    private val remoteTaskyItemDataSource: RemoteTaskyItemDataSource,
    private val applicationScope: CoroutineScope,
    private val notificationService: NotificationService
): AgendaRepository {

    override fun getTaskyItems(): Flow<List<TaskyItem>> {
        return localTaskyItemDataSource.getTaskyItems()
    }

    override fun getTaskyItemsForDate(localDateString: String): Flow<List<TaskyItem>> {
        return localTaskyItemDataSource.getTaskyItemsForDate(localDateString = localDateString)
    }

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        return when (val result = remoteTaskyItemDataSource.getFullAgenda()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localTaskyItemDataSource.upsertFullAgenda(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun getTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId): TaskyItem {
        return when (taskyType) {
            TaskyType.REMINDER -> {
                localTaskyItemDataSource.getTaskyItem(taskyType, taskyItemId)
            }

            TaskyType.TASK -> {
                localTaskyItemDataSource.getTaskyItem(taskyType, taskyItemId)
            }

            TaskyType.EVENT -> {
                localTaskyItemDataSource.getTaskyItem(taskyType, taskyItemId)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun upsertTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {
        val localResult = localTaskyItemDataSource.upsertTaskyItem(taskyItem)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        notificationService.scheduleNotification(taskyItem.toAgendaNotification())

        val taskyItemWithId = taskyItem.copy(id = localResult.data)

        val result = if (taskyItemWithId.type != TaskyType.EVENT) {
            when (val remoteResult = remoteTaskyItemDataSource.postTaskyItem(taskyItemWithId)) {
                is Result.Error -> {
                    // TODO: handle error case later
                    Result.Success(Unit)
                }
                is Result.Success -> {
                    applicationScope.async {
                        localTaskyItemDataSource.upsertTaskyItem(remoteResult.data)
                    }.await()

                }
            }

            // Handle Event upsert
        } else {
            when (val remoteEventResult = remoteTaskyItemDataSource.postTaskyEvent(taskyItemWithId)) {
                is Result.Error -> {
                    // TODO: handle error case later
                    remoteEventResult
                }
                is Result.Success -> {
                    val uploadedKeys = coroutineScope {
                        remoteEventResult.data.uploadUrls.map { uploadUrl ->
                            async {
                                val bytes = taskyItem.detailsAsEvent()?.photos?.firstOrNull {
                                    it.localPhotoKey == uploadUrl.photoKey
                                }?.compressedBytes
                                if (bytes != null) {
                                    when (remoteTaskyItemDataSource.uploadPhoto(url = uploadUrl.url, photo = bytes)) {
                                        is Result.Success -> uploadUrl.uploadKey
                                        is Result.Error -> null
                                    }
                                } else {
                                    null
                                }
                            }
                        }.awaitAll().filterNotNull()
                    }

                    if (uploadedKeys.isEmpty()) {
                        localTaskyItemDataSource.upsertTaskyItem(taskyItem = remoteEventResult.data.toTaskyItem())
                    } else {
                        // Send Photo Upload Confirmation POST
                        applicationScope.async {
                            val confirmResult = remoteTaskyItemDataSource.confirmPhotosUpload(
                                taskyItemId = taskyItemWithId.id,
                                confirmUploadRequest = ConfirmUploadRequest(
                                    uploadedKeys = uploadedKeys
                                )
                            )
                            when (confirmResult) {
                                is Result.Success -> {
                                    notificationService.scheduleNotification(taskyItem.toAgendaNotification())
                                    localTaskyItemDataSource.upsertTaskyItem(taskyItem = confirmResult.data.toTaskyItem())

                                }

                                is Result.Error -> {
                                    // TODO: handle error case later
                                    Result.Success(Unit) // Returning a success placeholder
                                }
                            }
                        }.await()

                    }
                }
            }
        }
        return result.asEmptyDataResult()
    }

    override suspend fun updateTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {

        val result = if (taskyItem.type != TaskyType.EVENT) {
            when (val newRemoteResult = remoteTaskyItemDataSource.updateTaskyItem(taskyItem)) {
                is Result.Error -> {
                    // TODO: handle error case later
                    Success(Unit)
                }

                is Result.Success -> {
                    applicationScope.async {
                        notificationService.scheduleNotification(taskyItem.toAgendaNotification())
                        localTaskyItemDataSource.upsertTaskyItem(newRemoteResult.data)
                            .asEmptyDataResult()
                    }.await()
                }
            }

            // Handle Event Update
        } else {
            when (val remoteEventResult = remoteTaskyItemDataSource.updateTaskyEvent(taskyItem)) {
                is Result.Error -> {
                    // TODO: handle error case later
                    remoteEventResult
                }

                is Result.Success -> {
                    val uploadedKeys = coroutineScope {
                        remoteEventResult.data.uploadUrls.map { uploadUrl ->
                            async {
                                val bytes = taskyItem.detailsAsEvent()?.photos?.firstOrNull() {
                                    it.localPhotoKey == uploadUrl.photoKey
                                }?.compressedBytes
                                if (bytes != null) {
                                    when (remoteTaskyItemDataSource.uploadPhoto(
                                        url = uploadUrl.url,
                                        photo = bytes
                                    )) {
                                        is Result.Success -> uploadUrl.uploadKey
                                        is Result.Error -> null
                                    }
                                } else {
                                    null
                                }
                            }

                        }.awaitAll().filterNotNull()
                    }
                    if(uploadedKeys.isEmpty()) {
                        localTaskyItemDataSource.upsertTaskyItem(taskyItem = remoteEventResult.data.toTaskyItem())

                    } else {
                        // Send Photo Upload Confirmation Post
                        applicationScope.async {
                            val confirmResult = remoteTaskyItemDataSource.confirmPhotosUpload(
                                taskyItemId = taskyItem.id,
                                confirmUploadRequest = ConfirmUploadRequest(
                                    uploadedKeys = uploadedKeys
                                )
                            )
                            when(confirmResult) {
                                is Result.Success -> {
                                    notificationService.scheduleNotification(taskyItem.toAgendaNotification())
                                    localTaskyItemDataSource.upsertTaskyItem(taskyItem = confirmResult.data.toTaskyItem())

                                }
                                is Result.Error -> {
                                    // TODO: handle error case later
                                    Success(Unit)
                                }
                            }

                        }.await()
                    }
                }


            }

        }
        return result.asEmptyDataResult()
    }

    override suspend fun deleteTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId): EmptyResult<DataError> {
        localTaskyItemDataSource.deleteTaskyItem(taskyType, taskyItemId)
        notificationService.cancelNotification(taskyItemId)
        val remoteResult = applicationScope.async {
            remoteTaskyItemDataSource.deleteTaskyItem(taskyItemId, taskyType)
        }.await()
        return when (remoteResult) {
            is Result.Error -> {
                // TODO: handle error case later
                Result.Success(Unit)
            } else -> return remoteResult.asEmptyDataResult()
        }
    }

    override suspend fun deleteAllLocalTaskyItems() {
        localTaskyItemDataSource.deleteAllTaskyItems()
    }
}
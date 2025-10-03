package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.LocalTaskyItemDataSource
import com.jvoye.tasky.core.domain.RemoteTaskyItemDataSource
import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstAgendaRepository(
    private val localTaskyItemDataSource: LocalTaskyItemDataSource,
    private val remoteTaskyItemDataSource: RemoteTaskyItemDataSource,
    private val applicationScope: CoroutineScope
): AgendaRepository {

    override fun getTaskyItems(): Flow<List<TaskyItem>> {
        return localTaskyItemDataSource.getTaskyItems()
    }

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        return when(val result = remoteTaskyItemDataSource.getFullAgenda()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localTaskyItemDataSource.upsertFullAgenda(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun getTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId): TaskyItem {
        return when(taskyType) {
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

    override suspend fun upsertTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {
        val localResult = localTaskyItemDataSource.upsertTaskyItem(taskyItem)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val taskyItemWithId = taskyItem.copy(id = localResult.data)
        val remoteResult = remoteTaskyItemDataSource.postTaskyItem(taskyItemWithId)
        return when (remoteResult) {
            is Result.Error -> {
                // TODO: handle error case later
                Result.Success(Unit)
            }
            is Result.Success -> {
                applicationScope.async {
                    localTaskyItemDataSource.upsertTaskyItem(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun updateTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {
        //TODO("Not yet implemented") Placeholder
        return Result.Success(Unit).asEmptyDataResult()
    }

    override suspend fun deleteTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId): EmptyResult<DataError> {
        localTaskyItemDataSource.deleteTaskyItem(taskyType, taskyItemId)
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
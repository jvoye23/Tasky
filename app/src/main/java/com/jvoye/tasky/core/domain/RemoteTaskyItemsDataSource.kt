package com.jvoye.tasky.core.domain

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result

interface RemoteTaskyItemDataSource {
    suspend fun getFullAgenda(): Result<FullAgenda, DataError.Network>
    suspend fun postTaskyItem(taskyItem: TaskyItem): Result<TaskyItem, DataError.Network>
    suspend fun updateTaskyItem(taskyItem: TaskyItem): Result<TaskyItem, DataError.Network>
    suspend fun deleteTaskyItem(taskyItemId: String, taskyType: TaskyType): EmptyResult<DataError.Network>

}
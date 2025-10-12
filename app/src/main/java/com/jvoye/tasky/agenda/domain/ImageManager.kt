package com.jvoye.tasky.agenda.domain

import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result

interface ImageManager {
    // Compress Images and save them in App's files directory
    suspend fun compressImages(uriStrings: List<String>): Result<List<String>, DataError.Local>
    suspend fun deleteAllCompressedImages(): EmptyResult<DataError.Local>

    fun createPhotoKeys(photos: List<String>, title: String): List<String>

    suspend fun filePathToLocalPhotoInfo(index: Int, filePath: String): LocalPhotoInfo
}



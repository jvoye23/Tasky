package com.jvoye.tasky.agenda.data

import android.content.Context
import androidx.core.net.toUri
import com.jvoye.tasky.agenda.domain.ImageManager
import com.jvoye.tasky.core.domain.DispatcherProvider
import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.util.ImageCompressor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import java.io.File

class AndroidImageManager(
    private val context: Context,
    private val imageCompressor: ImageCompressor,
    private val dispatcherProvider: DispatcherProvider
): ImageManager {

    override suspend fun compressImages(uriStrings: List<String>): Result<List<String>, DataError.Local> {
        return imageCompressor.processImages(uriStrings.map { it.toUri() })
    }

    override suspend fun deleteAllCompressedImages(): EmptyResult<DataError.Local> {
        return try {
            withContext(dispatcherProvider.io) {
                val directory: File = context.filesDir
                val filesToDelete = directory.listFiles { file ->
                    file.isFile && file.name.startsWith("compressed_")
                }

                filesToDelete?.forEach { file ->
                    file.delete()
                }
            }
            Result.Success(Unit)

        } catch (e: Exception) {
            if(e is CancellationException) throw e
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun createPhotoKeys(photos: List<String>, title: String): List<String> {
        return photos.mapIndexed { index, photo ->
            "${title}_photo0${index+1}"
        }
    }

    override suspend fun filePathToLocalPhotoInfo(index: Int, filePath: String): LocalPhotoInfo {
        return withContext(dispatcherProvider.io) {
            val file = File(filePath)
            val bytes = if (file.exists()) {
                file.inputStream().use { it.readBytes() }
            } else {
                byteArrayOf()
            }

            val fileName = "photo${index+1}"

            LocalPhotoInfo(
                localPhotoKey = fileName,
                filePath = filePath,
                compressedBytes = bytes
            )
       }
    }
}
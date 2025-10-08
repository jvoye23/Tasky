package com.jvoye.tasky.agenda.data

import android.content.Context
import android.net.Uri
import com.jvoye.tasky.agenda.domain.ImageManager
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.util.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AndroidImageManager(
    private val context: Context,
    private val imageCompressor: ImageCompressor,
): ImageManager {

    override suspend fun compressImages(uris: List<Uri>): Result<List<String>, DataError.Local> {
        return imageCompressor.processImages(uris)
    }

    override suspend fun deleteAllCompressedImages(): EmptyResult<DataError.Local> {
        return try {
            withContext(Dispatchers.IO) {
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
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
}
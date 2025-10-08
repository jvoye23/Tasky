package com.jvoye.tasky.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

class ImageCompressor(private val context: Context) {
    suspend fun processImages(uris: List<Uri>): Result<List<String>, DataError.Local> {
        return try {
            val processedPaths = withContext(Dispatchers.IO) {
                coroutineScope {
                    val deferredImageFiles = uris.map { uri ->
                        async {
                            compressImageFromUriAndCopyToAppFiles(context, uri)
                        }
                    }
                    // Wait for all async jobs to complete
                    val imageFiles: List<File?> = deferredImageFiles.awaitAll()

                    imageFiles.filterNotNull().map { it.absolutePath }
                }
            }

            Result.Success(processedPaths)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    private suspend fun compressImageFromUriAndCopyToAppFiles(context: Context, imageUri: Uri): File? {

        return withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            ensureActive()

            // Close the stream once decoded
            inputStream?.close()

            if (bitmap == null) {
                // Failed to decode the bitmap
                return@withContext null
            }

            var outputBytes: ByteArray

            // Start with a high quality and decrease it until the size is below maxSizeInBytes
            var quality = 100
            do {
                ByteArrayOutputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputBytes = outputStream.toByteArray()
                    // Clear the stream for the new attempt
                    outputStream.reset()
                    quality -= (quality * 0.1).roundToInt()
                }
            } while (isActive && outputBytes.size > MAX_IMAGE_SIZE_IN_BYTES && quality > 5)

            val compressedFileName = "compressed_${System.currentTimeMillis()}.jpg"
            val destinationFile = File(context.filesDir, compressedFileName)

            // Write the final compressed data from the in-memory stream to a new file
            FileOutputStream(destinationFile).use { fileOutputStream ->
                fileOutputStream.write(outputBytes)
            }
            return@withContext destinationFile
        }
    }


    companion object Companion {
        // The target maximum file size in bytes (1 MB)
        const val MAX_IMAGE_SIZE_IN_BYTES = 1 * 1024 * 1024
    }
}




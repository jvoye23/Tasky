package com.jvoye.tasky.core.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

fun compressImageFromUriAndCopyToAppFiles(context: Context, imageUri: Uri): File? {
    // The target maximum file size in bytes (1 MB)
    val maxSizeInBytes = 1 * 1024 * 1024

    return try {
        // Decode the image from the Uri's InputStream into a Bitmap
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Close the stream once decoded
        inputStream?.close()

        if (bitmap == null) {
            // Failed to decode the bitmap
            return null
        }

        // Use a ByteArrayOutputStream to hold the compressed image data in memory
        val outputStream = ByteArrayOutputStream()

        // Start with a high quality and decrease it until the size is below maxSizeInBytes
        var quality = 95
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        while (outputStream.size() > maxSizeInBytes && quality > 20) {
            quality -= 5

            // Clear the stream for the new attempt
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        // Write the final compressed data from the in-memory stream to a new file
        val compressedFileName = "compressed_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(context.filesDir, compressedFileName)

        FileOutputStream(destinationFile).use { fileOutputStream ->
            fileOutputStream.write(outputStream.toByteArray())
        }

        // Return the resulting compressed file
        destinationFile

    } catch (e: Exception) {
        e.printStackTrace()
        // Return null if any exception occurs
        null
    }
}

suspend fun deleteAllCompressedFiles(context: Context) {
    withContext(Dispatchers.IO) {
        val directory: File = context.filesDir

        val filesToDelete = directory.listFiles { file ->
            file.isFile && file.name.startsWith("compressed_")
        }

        filesToDelete?.forEach { file ->
            file.delete()
        }
    }
}




package com.jvoye.tasky.agenda.domain

import android.net.Uri
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result

interface ImageManager {
    suspend fun compressImages(uriStrings: List<String>): Result<List<String>, DataError.Local>
    suspend fun deleteAllCompressedImages(): EmptyResult<DataError.Local>
}



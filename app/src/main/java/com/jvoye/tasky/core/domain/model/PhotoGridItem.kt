package com.jvoye.tasky.core.domain.model

sealed class PhotoGridItem(open val key: String, open val path: String) {
    data class Remote(
        override val key: String,
        override val path: String
    ) : PhotoGridItem(key = key, path = path)

    data class Local(
        override val key: String,
        override val path: String
    ) : PhotoGridItem(key = key, path = path)
}

fun RemotePhoto.toPhotoGridItemTwo() = PhotoGridItem.Remote(
    key = key,
    path = url
)

fun LocalPhotoInfo.toPhotoGridItemTwo() = PhotoGridItem.Local(
    key = localPhotoKey,
    path = filePath
)
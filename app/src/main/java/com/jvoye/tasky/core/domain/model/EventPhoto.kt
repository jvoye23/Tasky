package com.jvoye.tasky.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EventPhoto(
    val id: Int
) : Parcelable
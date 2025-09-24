package com.jvoye.tasky.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Attendee(
    val name: String,
    val email: String,
) : Parcelable
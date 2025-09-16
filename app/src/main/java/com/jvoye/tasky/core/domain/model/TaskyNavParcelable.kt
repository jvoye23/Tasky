package com.jvoye.tasky.core.domain.model

import android.os.Parcelable
import com.jvoye.tasky.agenda.domain.TaskyType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TaskyNavParcelable(
    val taskyItemId: Long?,
    val taskyItemType: TaskyType,
    val isEditMode: Boolean
) : Parcelable

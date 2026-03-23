package com.gsm.taskmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
) : Parcelable
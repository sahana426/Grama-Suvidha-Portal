package com.gramasuvidha.portal.data.model

import androidx.annotation.StringRes
import com.gramasuvidha.portal.R

enum class ProjectStatus(val raw: String, @StringRes val labelRes: Int) {
    NotStarted("Not Started", R.string.status_not_started),
    InProgress("In Progress", R.string.status_in_progress),
    Completed("Completed", R.string.status_completed),
    Delayed("Delayed", R.string.status_delayed);

    companion object {
        fun fromRaw(raw: String) = entries.firstOrNull { it.raw == raw } ?: NotStarted
    }
}

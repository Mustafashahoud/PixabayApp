package com.mustafa.pixabayapp.utils

import android.annotation.SuppressLint
import android.text.TextUtils

object StringUtils {
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun getTags(tags: String?): String {
        return when {
            tags == null -> ""
            tags.contains(",") -> {
                val splitTags = tags.toUpperCase().split(", ")
                TextUtils.join(" - ", splitTags)
            }
            else -> tags
        }

    }

    @JvmStatic
    fun byUser(userName: String): String {
        return "By: $userName"
    }
}
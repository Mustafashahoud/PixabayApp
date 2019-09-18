package com.mustafa.pixabayapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["query"])
data class PhotoSearchResult(
    val query: String,
    val photoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)
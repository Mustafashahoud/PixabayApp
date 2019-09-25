package com.mustafa.pixabayapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["query", "pageNumber"])
data class PhotoSearchResult(
    val query: String,
    val photoIds: List<Int>,
    val pageNumber: Int
)
package com.mustafa.pixabayapp.network

import com.google.gson.annotations.SerializedName
import com.mustafa.pixabayapp.models.Photo

class PhotoSearchResponse(
    @SerializedName("totalHits")
    val totalHits: Int = 0,
    @SerializedName("hits")
    val photos: List<Photo>,
    @SerializedName("total")
    val total: Int
) {
    var next: Int? = null
}
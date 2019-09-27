package com.mustafa.pixabayapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    indices = [Index("id")])
data class Photo(
    @SerializedName("largeImageURL")
    val largeImageUrl : String,
    @SerializedName("webformatHeight")
    val webFormatHeight: Int,
    @SerializedName("webformatWidth")
    val webFormatWidth: Int,
    @SerializedName("likes")
    val likesCount: Int,
    @SerializedName("imageWidth")
    val imageWidth: Int,
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("views")
    val viewsCount: Long,
    @SerializedName("comments")
    val commentsCount: Int,
    @SerializedName("pageURL")
    val pageUrl : String,
    @SerializedName("imageHeight")
    val imageHeight: Int,
    @SerializedName("webformatURL")
    val webFormatURL: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("previewHeight")
    val previewHeight: Int,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("downloads")
    val downloadsCount: Int,
    @SerializedName("user")
    val userName: String,
    @SerializedName("favorites")
    val favoritesCount: Int,
    @SerializedName("imageSize")
    val imageSize: Long,
    @SerializedName("previewWidth")
    val previewWidth: Int,
    @SerializedName("userImageURL")
    val userImageUrl: String,
    @SerializedName("previewURL")
    val previewUrl: String

) : Serializable {

    /**
     * Empty constructor
     */
    constructor() :this ( "",
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        "",
        -1,
        "",
        "", -1,
        "",
        -1,
        "",
        -1,
        -1,
        -1,
        "",
        ""

    )
}
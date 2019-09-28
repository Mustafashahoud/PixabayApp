package com.mustafa.pixabayapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mustafa.pixabayapp.AppExecutors
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.database.PixBayDatabase
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.Resource
import com.mustafa.pixabayapp.network.ApiResponse
import com.mustafa.pixabayapp.network.PhotoSearchResponse
import com.mustafa.pixabayapp.network.PixBayService
import com.mustafa.pixabayapp.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDoa
) {
    fun loadPhotoById(photoId: Int): LiveData<Photo> {
        return photoDao.getPhotoById(photoId)
    }
}
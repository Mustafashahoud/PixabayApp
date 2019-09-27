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
    private val appExecutors: AppExecutors,
    private val photoDao: PhotoDoa,
    private val db: PixBayDatabase
)  {


    var photo: Photo? = null
    val ff = MediatorLiveData<Photo>()
    fun loadPhotoById(photoId: Int): LiveData<Photo>{
        appExecutors.diskIO().execute { photo = photoDao.getPhotoByIdNot(photoId)}
        ff.value = photo
        return ff
    }

}
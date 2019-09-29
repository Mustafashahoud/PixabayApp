package com.mustafa.pixabayapp.repository

import androidx.lifecycle.LiveData
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.models.Photo
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDoa
) {
    fun loadPhotoById(photoId: Int): LiveData<Photo> {
        return photoDao.getPhotoById(photoId)
    }
}
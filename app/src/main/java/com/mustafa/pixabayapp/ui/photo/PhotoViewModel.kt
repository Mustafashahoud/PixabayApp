package com.mustafa.pixabayapp.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.repository.PhotoRepository
import javax.inject.Inject

class PhotoViewModel @Inject constructor(private val photoRepository: PhotoRepository) :
    ViewModel() {

    private val _photoId = MutableLiveData<Int>()

    val photo: LiveData<Photo> = Transformations
        .switchMap(_photoId) { id ->
            photoRepository.loadPhotoById(id)
        }


    fun setId(photoId: Int) {
//        if (_photoId.value == photoId){
//            return
//        }
        _photoId.value = photoId

    }

}
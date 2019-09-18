package com.mustafa.pixabayapp.ui.search

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.Resource
import com.mustafa.pixabayapp.repository.PhotosRepository
import com.mustafa.pixabayapp.ui.AbsentLiveData
import java.util.*
import javax.inject.Inject

class SearchPhotoViewModel @Inject constructor(photoRepository: PhotosRepository) : ViewModel(){

    private val _query = MutableLiveData<String>()

    val query = _query

    val results: LiveData<Resource<List<Photo>>> =
        Transformations.switchMap(_query) { search ->
            if (search.isNullOrBlank()) {
                AbsentLiveData.create()
            }else {
                photoRepository.search(search, 1)
            }
        }


    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
//        nextPageHandler.reset()
        _query.value = input
    }

    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
    }
}
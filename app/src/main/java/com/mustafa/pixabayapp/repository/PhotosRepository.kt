package com.mustafa.pixabayapp.repository

import android.provider.SyncStateContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mustafa.pixabayapp.AppExecutors
import com.mustafa.pixabayapp.database.BixABayDatabase
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.di.BixBayApp
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.PhotoSearchResult
import com.mustafa.pixabayapp.models.Resource
import com.mustafa.pixabayapp.network.ApiResponse
import com.mustafa.pixabayapp.network.ApiSuccessResponse
import com.mustafa.pixabayapp.network.BixABayService
import com.mustafa.pixabayapp.network.SearchPhotoResponse
import com.mustafa.pixabayapp.ui.AbsentLiveData
import com.mustafa.pixabayapp.utils.Constants
import com.mustafa.pixabayapp.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotosRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: BixABayDatabase,
    private val photoDao: PhotoDoa,
    private val bixABayService: BixABayService
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    /**
     *
     * @param query
     * @param pageNumber
     * @return
     */
    fun search(query: String, pageNumber: Int): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>, SearchPhotoResponse>(appExecutors) {
            override fun saveCallResult(item: SearchPhotoResponse) {
                val photoIds: List<Int> = item.photos.map { it.id }
                val photoResult = PhotoSearchResult(
                    query = query,
                    photoIds = photoIds,
                    totalCount = item.total,
                    next = item.nexPage
                )

                db.runInTransaction{
                    photoDao.insertPhotos(item.photos)
                    photoDao.insert(photoResult)
                }

            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(query)
            }

            override fun loadFromDb(): LiveData<List<Photo>> {
                return Transformations.switchMap(photoDao.search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        photoDao.loadOrdered(searchData.photoIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<SearchPhotoResponse>> {
                return bixABayService.searchPhotos(Constants.API_KEY, query, pageNumber)
            }

        }.asLiveData()
    }
}
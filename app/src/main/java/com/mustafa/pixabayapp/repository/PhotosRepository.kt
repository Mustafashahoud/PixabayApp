package com.mustafa.pixabayapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mustafa.pixabayapp.AppExecutors
import com.mustafa.pixabayapp.database.PixBayDatabase
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.PhotoSearchResult
import com.mustafa.pixabayapp.models.Resource
import com.mustafa.pixabayapp.api.ApiResponse
import com.mustafa.pixabayapp.api.PixBayService
import com.mustafa.pixabayapp.api.PhotoSearchResponse
import com.mustafa.pixabayapp.utils.AbsentLiveData
import com.mustafa.pixabayapp.utils.Constants
import com.mustafa.pixabayapp.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: PixBayDatabase,
    private val photoDao: PhotoDoa,
    private val pixBayService: PixBayService
) {

    private val photoListRateLimit = RateLimiter<String>(60, TimeUnit.MINUTES)

    /**
     * @param query
     * @return the photos form Database directly if they exist or fetch them form Api service and then save them then load them form Db.
     * This is what so-called single source of truth https://developer.android.com/jetpack/docs/guide#truth
     */
    fun search(query: String, pageNumber: Int): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>, PhotoSearchResponse>(appExecutors) {
            override fun saveCallResult(item: PhotoSearchResponse) {

                val ids = arrayListOf<Int>()
                val photoIds: List<Int> = item.photos.map { it.id }


                if (pageNumber != 1 ) {
                    val prevPageNumber = pageNumber - 1
                    val photoSearchResult = photoDao.searchResult(query, prevPageNumber)
                    ids.addAll(photoSearchResult.photoIds)
                }

                ids.addAll(photoIds)
                val photoResult = PhotoSearchResult(
                    query = query,
                    photoIds = ids,
                    pageNumber = pageNumber
                )

                db.runInTransaction {
                    photoDao.insertPhotos(item.photos)
                    photoDao.insert(photoResult)
                }

            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                return data == null || data.isEmpty() || photoListRateLimit.shouldFetch(query)
            }

            override fun loadFromDb(): LiveData<List<Photo>> { // at the Very beginning When pageNumber = 1 --->(query, 2) -> null
                return Transformations.switchMap(photoDao.search(query, pageNumber)) { searchData ->

                    if (searchData == null ) {
                        AbsentLiveData.create()
                    } else {
                        photoDao.loadOrdered(searchData.photoIds)
                    }
                }
            }

            override fun onFetchFailed() {
                photoListRateLimit.reset(query)
            }

            override fun createCall(): LiveData<ApiResponse<PhotoSearchResponse>> {
                return pixBayService.searchPhotos(Constants.API_KEY, query, pageNumber)
            }

        }.asLiveData()
    }
}
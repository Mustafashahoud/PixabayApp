package com.mustafa.pixabayapp.database

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.PhotoSearchResult
import java.util.*

@Dao
abstract class PhotoDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createPhotoIfNotExists(photo: Photo): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(result: PhotoSearchResult)

    @Query("SELECT * FROM PhotoSearchResult WHERE `query` = :query AND pageNumber = :pageNumber ")
    abstract fun search(query: String, pageNumber: Int): LiveData<PhotoSearchResult>

    @Query("SELECT * FROM PhotoSearchResult WHERE `query` = :query AND pageNumber = :pageNumber ")
    abstract fun searchResult(query: String, pageNumber: Int): PhotoSearchResult

    @Query("SELECT * FROM PhotoSearchResult WHERE `query` = :query")
    abstract fun findSearchResult(query: String): PhotoSearchResult?

    @Query("SELECT * FROM Photo WHERE id in (:photoIds)")
    abstract fun loadById(photoIds: List<Int>): LiveData<List<Photo>>

    @Query("UPDATE PhotoSearchResult SET `query` = :query, photoIds = :photoIds, pageNumber = :pageNumber WHERE `query` = :query")
    abstract fun updatePhotoSearchResult(query: String, photoIds: List<Int>, pageNumber: Int)

    @Query("SELECT pageNumber FROM PhotoSearchResult WHERE `query` = :query")
    abstract fun getNextPage(query: String): Int

    @Query(" SELECT pageNumber FROM PhotoSearchResult WHERE `query` = :query")
    abstract fun getPageNumber (query: String): Int




    fun loadOrdered(photoIds: List<Int>): LiveData<List<Photo>> {
        val order = SparseIntArray()
        photoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(photoIds), fun(photos: List<Photo>): List<Photo>? {
            @Suppress("JavaCollectionsStaticMethodOnImmutableList")
            Collections.sort(photos) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            return photos
        })
    }
}
package com.mustafa.pixabayapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.models.PhotoSearchResult


@Database(
    entities = [
        Photo::class,
        PhotoSearchResult::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BixABayDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDoa
}
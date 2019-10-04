package com.mustafa.pixabayapp.di

import android.app.Application
import androidx.room.Room
import com.mustafa.pixabayapp.database.PixBayDatabase
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.network.PixBayService
import com.mustafa.pixabayapp.utils.Constants
import com.mustafa.pixabayapp.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePixBayService(): PixBayService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(PixBayService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): PixBayDatabase {
        return Room
            .databaseBuilder(app, PixBayDatabase::class.java, "PixaBay.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(db: PixBayDatabase): PhotoDoa {
        return db.photoDao()
    }
}
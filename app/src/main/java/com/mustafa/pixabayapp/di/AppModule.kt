package com.mustafa.pixabayapp.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.mustafa.pixabayapp.R
import com.mustafa.pixabayapp.database.BixABayDatabase
import com.mustafa.pixabayapp.database.PhotoDoa
import com.mustafa.pixabayapp.network.BixABayService
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
    fun provideGithubService(): BixABayService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(BixABayService::class.java)
    }

    @Singleton
    @Provides
    fun provideRequestGlideOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.white_background)
            .error(R.drawable.white_background)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions) : RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)

    }

    @Singleton
    @Provides
    fun provideDb(app: Application): BixABayDatabase {
        return Room
            .databaseBuilder(app, BixABayDatabase::class.java, "PixaBay.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(db: BixABayDatabase): PhotoDoa {
        return db.photoDao()
    }
}
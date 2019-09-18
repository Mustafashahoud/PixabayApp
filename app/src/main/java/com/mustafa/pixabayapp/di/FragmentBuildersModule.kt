package com.mustafa.pixabayapp.di

import com.mustafa.pixabayapp.ui.photo.PhotoFragment
import com.mustafa.pixabayapp.ui.search.SearchPhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributePhotoFragment(): PhotoFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchPhotoFragment(): SearchPhotoFragment
}
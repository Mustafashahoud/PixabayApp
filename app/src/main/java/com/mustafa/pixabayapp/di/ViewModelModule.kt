package com.mustafa.pixabayapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mustafa.pixabayapp.ui.photo.PhotoViewModel
import com.mustafa.pixabayapp.ui.search.SearchPhotoViewModel
import com.mustafa.pixabayapp.viewmodels.PBViewModelFactory
import com.mustafa.pixabayapp.viewmodels.PBViewModelFactory_Factory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchPhotoViewModel::class)
    abstract fun bindSearchPhotoViewModel(searchPhotoViewModel: SearchPhotoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel::class)
    abstract fun bindPhotoViewModel(photoViewModel: PhotoViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: PBViewModelFactory): ViewModelProvider.Factory
}
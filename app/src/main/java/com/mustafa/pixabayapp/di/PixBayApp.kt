package com.mustafa.pixabayapp.di

import android.app.Application
import dagger.android.*
import javax.inject.Inject
 // HasAndroidInjector instead of HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector, HasBroadcastReceiverInjector
// better boilerplate
class PixBayApp : Application (), HasAndroidInjector{

     @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
//        DaggerAppComponent.builder().application(this).build().inject(this);
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}
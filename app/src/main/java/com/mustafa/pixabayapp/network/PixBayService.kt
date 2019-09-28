package com.mustafa.pixabayapp.network

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixBayService {

// posts?userId=1
// @Path for Slash
// @Query for ?

    //api/?key=12294933-20224fc74d2113fc2413eafab&q=yellow+flowers&image_type=photo&pretty=true
    @GET("api/")
    fun searchPhotos (
        @Query("key") ApiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int
        ) : LiveData<ApiResponse<PhotoSearchResponse>> // it can be Flowable when Using RX-Java2

    @GET("api/")
    fun searchPhotos (
        @Query("key") ApiKey: String,
        @Query("q") query: String
    ) : LiveData<ApiResponse<PhotoSearchResponse>>
}
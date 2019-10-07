package com.mustafa.pixabayapp.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mustafa.pixabay.util.LiveDataTestUtil.getValue
import com.mustafa.pixabayapp.utils.Constants
import com.mustafa.pixabayapp.utils.LiveDataCallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class PixBayServiceTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: PixBayService
    private lateinit var mockWebServer: MockWebServer


    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(PixBayService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }


    @Test
    fun getPhotos() {
        enqueueResponse("search.json")
        val response = getValue(service.searchPhotos(Constants.API_KEY, "Test", 1)) as ApiSuccessResponse
        assertThat(response, notNullValue())
        assertThat(response.body.total, CoreMatchers.`is`(4495))
        assertThat(response.body.photos.size, CoreMatchers.`is`(20))
        assertThat(response.body.totalHits, CoreMatchers.`is`(500))
    }

    private fun enqueueResponse(fileName: String) {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val inputStream = javaClass.classLoader
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
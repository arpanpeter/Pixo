
package com.pixaby.arpan.arch.api

import com.pixaby.arpan.BuildConfig
import com.pixaby.arpan.model.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    companion object{
        const val BASE_URL = "https://pixabay.com/"
    }

    @GET("api/")
    suspend fun getImages(
        @Query("q") query: String = "",
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ) : Image
}
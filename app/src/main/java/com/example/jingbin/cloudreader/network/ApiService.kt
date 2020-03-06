package com.example.jingbin.cloudreader.network

import com.example.jingbin.cloudreader.dataclass.JokesDataClass
import com.example.jingbin.cloudreader.dataclass.PixabayDataClass
import com.example.jingbin.cloudreader.dataclass.SquareDataClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author One
 * @time 2019/11/30 14:41
 * @version 1.0
 */
interface ApiService {

    @GET("article/list/text")
    suspend fun remoteArticleData(@Query("page") page: Int): JokesDataClass

    @GET("user_article/list/{page}/json")
    suspend fun remoteSquareArticleData(@Path("page") page: Int): SquareDataClass

    @GET("api/")
    suspend fun remotePixabayDrawableData(
            @Query("key") key: String,
            @Query("q") q: String,
            @Query("page") page: Int,
            @Query("per_page") per_page: Int,
            @Query("image_type") image_type: String

    ): PixabayDataClass
}
package com.simplecode01.assessment3.network

import com.simplecode01.assessment3.model.Items
import com.simplecode01.assessment3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://unspoken.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WarehouseApiService {
    @GET("api_putra.php")
    suspend fun getItem(
        @Header("Authorization") userId: String
    ): List<Items>

    @Multipart
    @POST("api_putra.php")
    suspend fun postItem(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("detail") detail: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("api_putra.php")
        suspend fun deleteItem(
            @Header("Authorization") userId: String,
            @Query("id") id : String
    ): OpStatus
}

object WarehouseApi {
    val service: WarehouseApiService by lazy {
        retrofit.create(WarehouseApiService::class.java)
    }

    fun getItemsUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }

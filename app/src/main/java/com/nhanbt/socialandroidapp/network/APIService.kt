package com.nhanbt.socialandroidapp.network

import com.nhanbt.socialandroidapp.data.Result
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @Headers("Content-Type: application/json")
    @GET("{endpoint}")
    suspend fun get(@Path("endpoint") endpoint: String?): Result<String>

    @Headers("Content-Type: application/json")
    @POST("{endpoint}")
    suspend fun post(@Path("endpoint") endPoint: String?, @Body request: String): Result<String>

    @Headers("Content-Type: application/json")
    @POST("")
    fun refreshToken(@Body request: String): Call<String>
}

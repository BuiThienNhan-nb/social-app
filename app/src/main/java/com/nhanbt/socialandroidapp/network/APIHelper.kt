package com.nhanbt.socialandroidapp.network

import com.nhanbt.socialandroidapp.utils.Utils
import retrofit2.Response
import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.data.source.remote.ErrorResponse

object APIHelper {

    inline fun <reified T : Any> parseResponse(response: Response<String>): Result<T> {
        return try {
            // Response success
            if (response.isSuccessful) {
                val success: T? = response.body()?.let { Utils.fromJson(it, T::class.java) }
                Result.Success(success!!)
            }
            // Response has errors
            else {
                val error = response.errorBody()
                    ?.let { Utils.fromJson(it.string(), ErrorResponse::class.java) }
                Result.Error(error?.errors?.get(0)?.message.toString())
            }
        } catch (exception: Exception) {
            Result.Error(exception.message.toString())
        }
    }
}

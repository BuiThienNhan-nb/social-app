package com.nhanbt.socialandroidapp.data.source.remote

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(

    @SerializedName("data")
    val data: T? = null
)

data class ErrorResponse(

    @SerializedName("errors")
    val errors: List<ErrorsItem>? = null
)

data class ErrorsItem(

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)

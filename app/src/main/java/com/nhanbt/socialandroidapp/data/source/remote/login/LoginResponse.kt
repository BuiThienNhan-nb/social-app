package com.nhanbt.socialandroidapp.data.source.remote.login

import com.google.gson.annotations.SerializedName
import com.nhanbt.socialandroidapp.data.source.remote.BaseResponse

class LoginResponse : BaseResponse<LoginResponse>() {
    @SerializedName("user")
    var user: User? = null
}

data class User(
    @SerializedName("accessToken")
    val accessToken: String? = null,

    @SerializedName("refreshToken")
    val refreshToken: String? = null,

    @SerializedName("tokenExpireDate")
    val tokenExpireDate: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("avatarPath")
    val avatarPath: String? = null
)

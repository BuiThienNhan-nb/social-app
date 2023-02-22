package com.nhanbt.socialandroidapp.data.source.remote

import com.google.gson.annotations.SerializedName

class TokenResponse : BaseResponse<TokenResponse>() {

    @SerializedName("refreshTokenUserApp")
    val refreshTokenUserApp: RefreshTokenUserApp? = null
}

data class RefreshTokenUserApp(

    @SerializedName("accessToken")
    val accessToken: String? = null,

    @SerializedName("refreshToken")
    val refreshToken: String? = null
)

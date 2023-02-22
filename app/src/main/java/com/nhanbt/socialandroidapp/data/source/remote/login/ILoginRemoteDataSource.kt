package com.nhanbt.socialandroidapp.data.source.remote.login

import com.nhanbt.socialandroidapp.data.Result

interface ILoginRemoteDataSource {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}

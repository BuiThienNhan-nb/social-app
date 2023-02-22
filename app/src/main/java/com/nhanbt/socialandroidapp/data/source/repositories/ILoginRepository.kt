package com.nhanbt.socialandroidapp.data.source.repositories

import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.data.source.remote.login.LoginResponse

interface ILoginRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}

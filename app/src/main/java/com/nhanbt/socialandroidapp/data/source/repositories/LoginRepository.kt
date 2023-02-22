package com.nhanbt.socialandroidapp.data.source.repositories

import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.data.source.remote.login.ILoginRemoteDataSource
import com.nhanbt.socialandroidapp.data.source.remote.login.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val dataSource: ILoginRemoteDataSource
) : ILoginRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return dataSource.login(email, password)
    }
}

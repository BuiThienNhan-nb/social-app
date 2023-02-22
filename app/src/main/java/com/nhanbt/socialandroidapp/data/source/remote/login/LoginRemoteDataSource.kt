package com.nhanbt.socialandroidapp.data.source.remote.login

import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.network.APIService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRemoteDataSource @Inject constructor(
    private val apiService: APIService
) : ILoginRemoteDataSource {

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        // Fake login implementation
        val value = GlobalScope.async {
            delay(1500L)
            println("thread running on [${Thread.currentThread().name}]")
            10
        }
        println("value =  ${value.await()} thread running on [${Thread.currentThread().name}]")
        if (email.compareTo("test@gmail.com") != 0) {
            return Result.Error("This email haven\'t been registered yet!")
        }
        if (password.compareTo("Test@12345") != 0) {
            return Result.Error("Wrong password!")
        }
        val response = LoginResponse()
        response.user = User(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenExpireDate = "timestamp",
            userId = "id",
            name = "Test",
            email = email,
        )
        return Result.Success(response)
    }
}

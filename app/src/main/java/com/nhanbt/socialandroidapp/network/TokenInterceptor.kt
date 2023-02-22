package com.nhanbt.socialandroidapp.network

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.nhanbt.socialandroidapp.BuildConfig
import com.nhanbt.socialandroidapp.R
import com.nhanbt.socialandroidapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.data.source.remote.TokenResponse

class TokenInterceptor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Original request
        var originalRequest = chain.request()

        // Original request builder
        val builder = originalRequest.newBuilder()

        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")

        // Overwrite the original request
        originalRequest = builder.build()

        // Perform request, here original request will be executed
        val response = chain.proceed(originalRequest)

        // If get the status code is unauthorized
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {

            // Perform all 401 in sync blocks, to avoid multiply token updates
            synchronized(this) {

                val currentToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
                // Compare current token with token that was stored before, if it was not updated - do update
                if (!TextUtils.isEmpty(currentToken)
                    && TextUtils.equals(currentToken, accessToken)
                ) {
                    // Refresh token
                    refreshToken()

                    // If access token is valid
                    if (!TextUtils.isEmpty(
                            sharedPreferences.getString(
                                Constants.ACCESS_TOKEN,
                                ""
                            )
                        )
                    ) {
                        // Re-build originalRequest
                        originalRequest = builder.build()

                        // Close previous response before retry request
                        response.close()

                        // Retry request with new token
                        return chain.proceed(originalRequest)
                    }
                    // Throw session expired exception, user need to log in again
                    else {
                        throw SessionExpiredException(context)
                    }
                }
            }
        }

        return response
    }

    private fun refreshToken() {
        var retryCount = 0
        val refreshToken = sharedPreferences.getString(Constants.REFRESH_TOKEN, "")

        do {
            if (refreshToken != null && !refreshToken.isNullOrEmpty()) {
                // Fetch new token from server
                val request = JSONObject().put(
                    "query",
                    "mutation {" +
                            "    refreshTokenUserApp(condition: {" +
                            "        refreshToken: \"$refreshToken\"" +
                            "    }) {" +
                            "        accessToken," +
                            "        refreshToken" +
                            "    }" +
                            "}"
                )

                // Refresh token synchronously
                val response = getAPIService().refreshToken(request.toString()).execute()
                // If response returned successful
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val tokenResponse = APIHelper.parseResponse<TokenResponse>(response)
                    if (tokenResponse is Result.Success) {
                        // Save access token, refresh token to local data source
                        sharedPreferences.edit()?.apply {
                            putString(
                                Constants.ACCESS_TOKEN,
                                tokenResponse.data.data?.refreshTokenUserApp?.accessToken
                            )
                            putString(
                                Constants.REFRESH_TOKEN,
                                tokenResponse.data.data?.refreshTokenUserApp?.refreshToken
                            )
                            apply()
                        }
                    }
                }
                // Retry if failed
                else {
                    retryCount++
                    continue
                }
            }

            // Reached here, will not retry, end loop
            break

        }
        // Retry with three-times max
        while (retryCount <= Constants.TOKEN_MAX_RETRY_COUNT)

        // If refresh token not refreshed
        if (TextUtils.equals(
                refreshToken,
                sharedPreferences.getString(Constants.REFRESH_TOKEN, "")
            )
        ) {
            // Clear user credentials
            clearUserCredentials()
        }
    }

    private fun clearUserCredentials() {
        sharedPreferences.edit()?.apply {
            remove(Constants.ACCESS_TOKEN)
            remove(Constants.REFRESH_TOKEN)
//            remove(Constants.DISPLAY_NAME)
            apply()
        }
    }

    private fun getAPIService(): APIService {
        // Logger
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            // Config connection
            .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .callTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(APIService::class.java)
    }
}

class SessionExpiredException(private val context: Context) : IOException() {

    override val message: String
        get() = context.getString(R.string.error_session_expired)
}

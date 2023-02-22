package com.nhanbt.socialandroidapp.locators

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.nhanbt.socialandroidapp.BuildConfig
import com.nhanbt.socialandroidapp.network.APIService
import com.nhanbt.socialandroidapp.network.HeaderInterceptor
import com.nhanbt.socialandroidapp.network.NetworkConnectionInterceptor
import com.nhanbt.socialandroidapp.network.TokenInterceptor
import com.nhanbt.socialandroidapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAPIService(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
    ): APIService {
        // Logger
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        // Interceptor to add "access_token" as query parameters to all
        val authInterceptor = Interceptor { chain ->
            // Original request
            val request = chain.request()
            val newBuilder = request.newBuilder()
            val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "")
            if (!TextUtils.isEmpty(accessToken)) {
                newBuilder.addHeader(Constants.AUTHORIZATION_HEADER, "Bearer $accessToken")
            }
            // Pass request down the chain, and get the response synchronously
            chain.proceed(newBuilder.build())
        }

        val okHttpClient = OkHttpClient.Builder()
            // Add interceptors
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(TokenInterceptor(context, sharedPreferences))
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            // Config connection
            .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .callTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .build()

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "social_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}

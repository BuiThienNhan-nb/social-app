package com.nhanbt.socialandroidapp.locators

import com.nhanbt.socialandroidapp.data.source.remote.login.ILoginRemoteDataSource
import com.nhanbt.socialandroidapp.data.source.remote.login.LoginRemoteDataSource
import com.nhanbt.socialandroidapp.data.source.repositories.ILoginRepository
import com.nhanbt.socialandroidapp.data.source.repositories.LoginRepository
import com.nhanbt.socialandroidapp.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    fun provideLoginRemoteDataSource(
        apiService: APIService
    ): ILoginRemoteDataSource = LoginRemoteDataSource(apiService)

    @Provides
    fun provideLoginRepository(
        dataSource: ILoginRemoteDataSource
    ): ILoginRepository = LoginRepository(dataSource)
}

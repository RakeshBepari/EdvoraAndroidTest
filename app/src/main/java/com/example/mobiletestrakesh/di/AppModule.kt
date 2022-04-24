package com.example.mobiletestrakesh.di

import com.example.mobiletestrakesh.data.remote.UserRidesApi
import com.example.mobiletestrakesh.data.repository.DefaultUserRidesRepository
import com.example.mobiletestrakesh.domain.repository.UserRidesRepository
import com.example.mobiletestrakesh.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance() : UserRidesApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(UserRidesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDefaultRepository(userRidesApi: UserRidesApi) =
        DefaultUserRidesRepository(userRidesApi) as UserRidesRepository


}
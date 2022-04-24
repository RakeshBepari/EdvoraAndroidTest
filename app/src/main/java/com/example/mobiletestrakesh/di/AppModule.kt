package com.example.mobiletestrakesh.di

import android.app.Application
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.mobiletestrakesh.data.remote.UserRidesApi
import com.example.mobiletestrakesh.data.repository.DefaultUserRidesRepository
import com.example.mobiletestrakesh.domain.repository.UserRidesRepository
import com.example.mobiletestrakesh.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * A module for providing various dependencies like api and repository
 * in the scope of the application and there will be a single instance
 * of these object throughout the Application
 * */
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

    /**
     * Provides a repository of Interface type i.e.- UserRidesRepository
     * */
    @Provides
    @Singleton
    fun providesDefaultRepository(userRidesApi: UserRidesApi) =
        DefaultUserRidesRepository(userRidesApi) as UserRidesRepository

}
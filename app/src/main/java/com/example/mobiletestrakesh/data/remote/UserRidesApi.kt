package com.example.mobiletestrakesh.data.remote

import com.example.mobiletestrakesh.data.remote.responses.RidesDto
import com.example.mobiletestrakesh.data.remote.responses.UserDto
import retrofit2.Response
import retrofit2.http.GET


/**
 * Retrofit api for calling the remote api
 * which gives us list of rides and a user object
 * */
interface UserRidesApi {

    @GET("rides/")
    suspend fun getRidesDto(): RidesDto

    @GET("user/")
    suspend fun getUserDto(): UserDto
}
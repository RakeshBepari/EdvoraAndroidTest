package com.example.mobiletestrakesh.domain.repository

import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRidesRepository {

    suspend fun getRides(): Flow<Resource<List<RidesItem>>>

    suspend fun getUser(): Resource<User>
}
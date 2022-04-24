package com.example.mobiletestrakesh.domain.repository

import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction of the repository which is to be used in the ViewModel
 * */

interface UserRidesRepository {

    suspend fun getRides(): Flow<Resource<List<RidesItem>>>

    suspend fun getUser(): Resource<User>
}
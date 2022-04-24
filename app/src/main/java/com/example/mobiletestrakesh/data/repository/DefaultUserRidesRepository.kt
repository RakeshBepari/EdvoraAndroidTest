package com.example.mobiletestrakesh.data.repository

import com.example.mobiletestrakesh.data.mapper.toRidesItem
import com.example.mobiletestrakesh.data.mapper.toUser
import com.example.mobiletestrakesh.data.remote.UserRidesApi
import com.example.mobiletestrakesh.data.remote.responses.UserDto
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.domain.repository.UserRidesRepository
import com.example.mobiletestrakesh.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class DefaultUserRidesRepository @Inject constructor(
    private val api: UserRidesApi
) : UserRidesRepository {

    override suspend fun getRides(): Flow<Resource<List<RidesItem>>> {
        return flow {
            emit(Resource.Loading(true))

            val remoteRidesDto = try {
                api.getRidesDto()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load the data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Network Error"))
                null
            }

            remoteRidesDto?.let { ridesDto ->
                emit(Resource.Loading(isLoading = false))
                emit(
                    Resource.Success(
                        data = ridesDto.map {
                            it.toRidesItem()
                        })
                )
            }
        }
    }

    override suspend fun getUser(): Resource<User> {
        return try {
            val userDto = api.getUserDto()

            userDto.let {
                Resource.Success(data = it.toUser())
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load the data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Network Error")
        }
    }

}
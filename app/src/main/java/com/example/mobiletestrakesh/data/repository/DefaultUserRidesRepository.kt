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

/**
 * An implementation of interface UserRidesRepository
 *
 *  Depending on abstractions allows us to change the implementations at a later stage
 *  without changing the function definitions i.e.- like we can cache the data from api in
 *  and database then provide it further from the cache.
 *
 *  It also comes helpful in testing as we can create fake repositories and test with it instead
 *  doing actual network calls or database calls in actual repository
 * */
class DefaultUserRidesRepository @Inject constructor(
    private val api: UserRidesApi
) : UserRidesRepository {

    /**
     * Gets the list of rides from the api and emit different values
     * at different times based on api call was successful or failure
     * */
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
                            it.toRidesItem() /** the data layer object is mapped to Domain layer object */
                        })
                )
            }
        }
    }

    /**
     * Gets the user from the api and emit different values
     * at different times based on api call was successful or failure
     * */
    override suspend fun getUser(): Resource<User> {
        return try {
            val userDto = api.getUserDto()

            userDto.let {
                Resource.Success(
                    data = it.toUser() /** the data layer object is mapped to Domain layer object */
                )
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
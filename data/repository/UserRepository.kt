package com.example.composemaster.data.repository

import com.example.composemaster.data.local.dao.UserDao
import com.example.composemaster.data.local.entity.UserEntity
import com.example.composemaster.data.remote.api.UserApi
import com.example.composemaster.domain.model.User
import com.example.composemaster.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {
    fun getUsers(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())
        
        // Get users from local cache
        val localUsers = dao.getAllUsers().map { entities ->
            entities.map { it.toUser() }
        }
        
        try {
            // Try to fetch from API
            val remoteUsers = api.getUsers()
            // Clear old data and insert new
            dao.deleteAllUsers()
            dao.insertUsers(remoteUsers.map { UserEntity.fromUser(it.toUser()) })
        } catch (e: HttpException) {
            emit(Resource.Error("An HTTP error occurred: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Check your internet connection: ${e.message}"))
        }
        
        // Emit updated local data
        dao.getAllUsers().collect { entities ->
            emit(Resource.Success(entities.map { it.toUser() }))
        }
    }
    
    suspend fun addUser(user: User): Long {
        return dao.insertUser(UserEntity.fromUser(user))
    }
}

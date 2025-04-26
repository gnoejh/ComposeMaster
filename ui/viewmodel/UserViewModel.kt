package com.example.composemaster.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemaster.data.local.AppDatabase
import com.example.composemaster.data.remote.api.UserApi
import com.example.composemaster.data.repository.UserRepository
import com.example.composemaster.domain.model.User
import com.example.composemaster.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel(application: Application) : AndroidViewModel(application) {
    
    private val api = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)
        
    private val dao = AppDatabase.getInstance(application).userDao()
    private val repository = UserRepository(api, dao)
    
    private val _usersState = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val usersState: StateFlow<Resource<List<User>>> = _usersState
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers().collect { result ->
                _usersState.value = result
            }
        }
    }
    
    fun addUser(name: String, email: String) {
        viewModelScope.launch {
            repository.addUser(User(name = name, email = email))
        }
    }
}

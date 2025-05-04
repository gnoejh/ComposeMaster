package com.example.mvvmroomdatabase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)
}
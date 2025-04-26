package com.example.composemaster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composemaster.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val avatarUrl: String?
) {
    fun toUser(): User = User(id, name, email, avatarUrl)
    
    companion object {
        fun fromUser(user: User): UserEntity = 
            UserEntity(user.id, user.name, user.email, user.avatarUrl)
    }
}

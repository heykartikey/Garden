package com.example.garden.data.repository

data class User(
    val email: String,
    val password: String,
)

interface AuthRepository {
    suspend fun signIn(user: User): Boolean
}
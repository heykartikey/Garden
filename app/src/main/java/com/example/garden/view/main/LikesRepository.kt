package com.example.garden.view.main

import com.example.garden.data.model.Response
import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    suspend fun getLikes(userId: String): Flow<List<String>>
    suspend fun getLikeById(userId: String, feedId: String): Response<Boolean>
    suspend fun updateLike(userId: String, feedId: String, liked: Boolean)
}
package com.example.garden.data.model

sealed interface Response<out T> {
    object Loading : Response<Nothing>
    data class Success<out T>(val data: T?) : Response<T>
    data class Failure(val e: Exception) : Response<Nothing>
}
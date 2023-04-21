package com.example.garden.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val likedVideos: List<String> = listOf(),
)


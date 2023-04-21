package com.example.garden.domain.model

data class VideoDetails(
    val id: String = "",
    val title: String = "",
    val videoLink: String = "",
    val description: String = "",
    val images: List<String> = listOf(),
    val thumb: String = ""
)

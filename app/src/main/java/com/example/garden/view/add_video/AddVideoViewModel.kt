package com.example.garden.view.add_video

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.data.repository.FeedRepository
import com.example.garden.domain.model.VideoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVideoViewModel @Inject constructor(
    private val firebaseRepository: FeedRepository,
) : ViewModel() {
    var title by mutableStateOf("")
        private set

    var videoLink by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var images by mutableStateOf(emptyList<Uri>())
        private set

    var uploadStatus by mutableStateOf(false)

    fun addVideoDetails(videoDetails: VideoDetails, images: List<Uri>) {
        viewModelScope.launch {
            uploadStatus = false
            firebaseRepository.addVideoDetails(videoDetails, images)
            uploadStatus = true
        }
    }

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateVideoLink(videoLink: String) {
        this.videoLink = videoLink
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updateImages(images: List<Uri>) {
        this.images = images
    }

    fun resetState() {
        title = ""
        videoLink = ""
        description = ""
        images = listOf()
    }
}
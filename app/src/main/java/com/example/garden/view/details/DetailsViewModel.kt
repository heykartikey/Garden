package com.example.garden.view.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.data.repository.FeedRepository
import com.example.garden.domain.model.VideoDetails
import com.example.garden.view.main.LikesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FeedItemState {
    object Loading : FeedItemState
    data class Success(val data: VideoDetails) : FeedItemState
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val likesRepository: LikesRepository,
) : ViewModel() {
    private val _feedItemState = MutableStateFlow<FeedItemState>(FeedItemState.Loading)
    val feedItemState = _feedItemState.asStateFlow()

    private val _likes = MutableStateFlow(listOf<String>())
    val likes = _likes.asStateFlow()


    suspend fun getFeedDetails(feedId: String) {
        viewModelScope.launch {
            feedRepository.getFeedById(feedId).collect {
                _feedItemState.value = FeedItemState.Success(it)
            }
        }
    }

    suspend fun getLikes(user: String?) {
        when (user) {
            null -> _likes.value = listOf()
            else -> {
                viewModelScope.launch {
                    likesRepository.getLikes(user).collectLatest {
                        _likes.value = it
                    }
                }
            }
        }
    }

    fun updateLike(userId: String, feedId: String, liked: Boolean) {
        viewModelScope.launch {
            likesRepository.updateLike(userId, feedId, liked)
        }
    }
}
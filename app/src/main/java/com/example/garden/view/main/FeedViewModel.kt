package com.example.garden.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val likesRepository: LikesRepository,
) : ViewModel() {
    private val _feedState = MutableStateFlow<FeedState>(FeedState.Loading)
    val feedState = _feedState.asStateFlow()

    private val _likes = MutableStateFlow(listOf<String>())
    val likes = _likes.asStateFlow()

    var likesCoroutine: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            feedRepository.getFeed().collect {
                _feedState.value = FeedState.Success(it)
            }
        }
    }

    suspend fun getLikes(user: String?) {
        when (user) {
            null -> _likes.value = listOf()
            else -> {
                likesCoroutine = viewModelScope.launch {
                    likesRepository.getLikes(user).launchIn(viewModelScope)
                }
            }
        }
    }

    fun updateLike(userId: String, feedId: String, liked: Boolean) {
        viewModelScope.launch {
//            if (!(likesRepository as FirebaseLikesRepository).hasFlowStarted) {
//                launch {
//                    getLikes(userId)
//                }
//            }

            likesRepository.updateLike(userId, feedId, liked)
        }
    }
}
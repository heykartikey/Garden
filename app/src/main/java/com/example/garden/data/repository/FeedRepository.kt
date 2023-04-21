package com.example.garden.data.repository

import android.net.Uri
import com.example.garden.domain.model.VideoDetails
import com.example.garden.view.main.FeedItem
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    suspend fun getFeed(): Flow<List<FeedItem>>
    suspend fun getFeedById(feedId: String): Flow<VideoDetails>
    suspend fun addVideoDetails(videoDetails: VideoDetails, images: List<Uri>)
}
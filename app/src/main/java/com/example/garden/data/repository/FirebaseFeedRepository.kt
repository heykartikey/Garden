package com.example.garden.data.repository

import android.net.Uri
import com.example.garden.domain.model.VideoDetails
import com.example.garden.view.main.FeedItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirebaseFeedRepository(
    private val ref: DatabaseReference,
    private val storageRef: StorageReference,
) : FeedRepository {
    override suspend fun getFeed(): Flow<List<FeedItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.mapNotNull { it.getValue<FeedItem>()?.copy(id = it.key!!) }.let {
                    trySend(it.reversed())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    override suspend fun getFeedById(feedId: String): Flow<VideoDetails> {
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.getValue<VideoDetails>()
                            ?.let { trySend(it.copy(id = snapshot.key!!)) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            ref.child(feedId).addValueEventListener(listener)

            awaitClose {
                ref.child(feedId).addValueEventListener(listener)
            }
        }
    }

    override suspend fun addVideoDetails(videoDetails: VideoDetails, images: List<Uri>) {
        withContext(Dispatchers.IO) {
            val key = ref.push().key!!
            val stRef = storageRef.child(key)
            val downloads = mutableListOf<String>()

            images.chunked(2).map { chunked -> // TODO: Upload only two images at a time?
                val uploads = chunked.map {
                    async {
                        val fileName = UUID.randomUUID().toString()
                        val fileRef = stRef.child(fileName)

                        fileRef.putFile(it).await()
                        fileRef.downloadUrl.await().toString()
                    }
                }

                downloads.addAll(uploads.awaitAll())
            }

            println(downloads)

            ref.child(key).setValue(
                videoDetails.copy(
                    images = downloads.toList(),
                    thumb = downloads.firstOrNull() ?: ""
                )
            )
        }
    }
}
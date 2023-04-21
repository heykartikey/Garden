package com.example.garden.view.main

import android.util.Log
import com.example.garden.data.model.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseLikesRepository constructor(
    private val ref: DatabaseReference,
) : LikesRepository {
    override suspend fun getLikes(userId: String): Flow<List<String>> = callbackFlow {
        Log.d("Likes", "Flow Started")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    (snapshot.value as? Map<String, Any>)?.keys?.toList() ?: listOf()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.child(userId).addValueEventListener(listener)

        awaitClose {
            Log.d("Likes", "Flow Cancelled")
            ref.child(userId).removeEventListener(listener)
        }
    }.catch {
        Log.e("Likes", "Error observing likes for user $userId", it)
    }

    override suspend fun updateLike(userId: String, feedId: String, liked: Boolean) {
        withContext(Dispatchers.IO) {
            when (liked) {
                true -> {
                    ref.child(userId).child(feedId).setValue(true).await()
                    Log.d("Likes", "Liked $feedId by $userId")
                }

                false -> {
                    ref.child(userId).child(feedId).removeValue()
                    Log.d("Likes", "Disliked $feedId by $userId")
                }
            }
        }
    }

    override suspend fun getLikeById(userId: String, feedId: String): Response<Boolean> {
        return try {
            Response.Success(ref.child(userId).child(feedId).get().await().exists())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
package com.example.garden.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseEmailAuthRepository : AuthRepository {
    override suspend fun signIn(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Firebase.auth.signInWithEmailAndPassword(user.email, user.password).await()
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }
}
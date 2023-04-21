package com.example.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.data.enums.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthStateViewModel(
    private val firebaseAuth: FirebaseAuth = Firebase.auth,
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState.NOT_LOGGED_IN)
    val authState = _authState.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        if (firebaseAuth.currentUser != null) {
            viewModelScope.launch {
                val result = firebaseAuth.currentUser!!.getIdToken(true).await()
                when {
                    result.claims[ADMIN_KEY] == true -> _authState.value = AuthState.LOGGED_IN_ADMIN
                    else -> _authState.value = AuthState.LOGGED_ID_USER
                }
            }
        } else {
            _authState.value = AuthState.NOT_LOGGED_IN
        }
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    companion object {
        private const val ADMIN_KEY = "admin"
    }
}
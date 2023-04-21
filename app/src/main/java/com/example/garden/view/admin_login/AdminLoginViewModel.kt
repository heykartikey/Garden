package com.example.garden.view.admin_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garden.data.repository.AuthRepository
import com.example.garden.data.repository.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    var email by mutableStateOf("heykartikey@gmail.com")
        private set

    var password by mutableStateOf("12345678")
        private set

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun signIn(callbackOnSuccess: () -> Unit) {
        viewModelScope.launch {
            println("Login via repo here $email $password")

            if (authRepo.signIn(User(email = email, password = password))) {
                callbackOnSuccess()
            }
        }
    }
}
package com.example.garden.view.admin_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.garden.AppScreen
import com.example.garden.DayNightPreview
import com.example.garden.ui.theme.GardenTheme
import com.example.garden.view.components.AppBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AdminLoginScreen(
    onNavigateUp: () -> Unit, setEmail: (String) -> Unit, setPassword: (String) -> Unit,
    email: String, password: String, signIn: () -> Unit,
) {
    Scaffold(topBar = {
        AppBar(title = "Admin Login", onNavigateUp = onNavigateUp)
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp, alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            ) {
//            Text("Garden", style = MaterialTheme.typography.h1) // TODO: logo here
            Text("Sign In to continue", style = MaterialTheme.typography.h1)
            TextField(email, modifier = Modifier.fillMaxWidth(), onValueChange = setEmail, label = {
                Text("Email")
            }, leadingIcon = {
                Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
            })
            TextField(
                password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = setPassword,
                label = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = signIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Sign In")
            }
        }
    }
}

fun NavController.navigateToAdminLoginScreen() {
    navigate(AppScreen.AdminLoginScreen.route)
}

fun NavGraphBuilder.adminLoginScreen(
    onNavigateToAddVideoScreen: () -> Unit,
    onNavigateUp: () -> Unit,
) {
//    Firebase.auth.signOut() // make sure no one is logged in before attempting to login again
    composable(AppScreen.AdminLoginScreen.route) {
        val viewModel: AdminLoginViewModel = hiltViewModel()

        AdminLoginScreen(onNavigateUp = onNavigateUp,
            email = viewModel.email,
            password = viewModel.password,
            setEmail = { email -> viewModel.updateEmail(email) },
            setPassword = { password -> viewModel.updatePassword(password) },
            signIn = {
                viewModel.signIn {
                    onNavigateToAddVideoScreen()
                }
            })
    }
}

@DayNightPreview
@Composable
fun AdminLoginScreenPreview() {
    GardenTheme {
        AdminLoginScreen(
            onNavigateUp = {},
            setEmail = {},
            setPassword = { },
            email = "",
            password = "",
            signIn = {})
    }
}
package com.example.garden

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.garden.data.enums.AuthState
import com.example.garden.view.add_video.AddVideoViewModel
import com.example.garden.view.add_video.addVideoScreen
import com.example.garden.view.add_video.navigateToAddVideoScreen
import com.example.garden.view.admin_login.adminLoginScreen
import com.example.garden.view.admin_login.navigateToAdminLoginScreen
import com.example.garden.view.details.detailScreen
import com.example.garden.view.details.navigateToDetailScreen
import com.example.garden.view.main.mainScreen
import com.example.garden.view.main.navigateToMainScreen

enum class AppScreen(val route: String) {
    MainScreen(route = "main_screen"),
    AddVideoScreen(route = "add_video_screen"),
    DetailsScreen(route = "details_screen/{id}"),
    AdminLoginScreen(route = "admin_login_screen"),
    // TODO: Add other screens
}

@Composable
fun Navigation(
    addVideoViewModel: AddVideoViewModel,
    signInUser: () -> Unit,
    navController: NavHostController,
    showNotAdminDialog: () -> Unit,
    userId: String? = "",
    authState: AuthState
) {
    NavHost(navController = navController, startDestination = AppScreen.MainScreen.route) {
        mainScreen(
            authState = authState,
//            isAdmin = authState == AuthState.LOGGED_IN_ADMIN,
            onNavigateToDetailScreen = { id -> navController.navigateToDetailScreen(id) },
            onNavigateToAddVideoScreen = { navController.navigateToAddVideoScreen() },
            signInUser = signInUser,
            showNotAdminDialog = showNotAdminDialog,
            onNavigateToAdminLoginScreen = { navController.navigateToAdminLoginScreen() }
        )
        detailScreen(
            userId = userId,
            onNavigateUp = { navController.navigateToMainScreen() },
            signInUser = signInUser
        )
        adminLoginScreen(
            onNavigateToAddVideoScreen = { navController.navigateToAddVideoScreen() },
            onNavigateUp = { navController.navigateToMainScreen() },
        )
        addVideoScreen(
            onNavigateUp = { navController.navigateToMainScreen() },
            addVideoViewModel = addVideoViewModel,
        )
    }
}
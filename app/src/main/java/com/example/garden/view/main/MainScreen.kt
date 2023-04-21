package com.example.garden.view.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.garden.AppScreen
import com.example.garden.DayNightPreview
import com.example.garden.data.enums.AuthState
import com.example.garden.ui.theme.GardenTheme
import com.example.garden.view.components.AppBar
import com.example.garden.view.components.FeedItemCardExpanded
import com.example.garden.view.components.OverlayLoadingWheel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel

sealed interface FeedState {
    object Loading : FeedState
    data class Success(val feed: List<FeedItem>) : FeedState
}

data class FeedItem(
    val id: String = "",
    val title: String = "",
    val thumb: String = "",
)

@Composable
fun MainScreen(
    feedState: FeedState,
    likes: List<String>,
    onNavigateToDetailScreen: (String) -> Unit,
    onLikeChanged: (String, Boolean) -> Unit,
    onNavigateToAddVideoScreen: () -> Unit,
    onNavigateToAdminLoginScreen: () -> Unit,
    showNotAdminDialog: () -> Unit,
    isAdmin: Boolean
) {
    Scaffold(
        topBar = {
            AppBar(title = "Videos", showBackButton = false) {}
        },
        floatingActionButton = {
            AnimatedVisibility(visible = true) {
                FloatingActionButton(onClick = {
                    if (!isAdmin) {
                        showNotAdminDialog()
                    } else {
                        onNavigateToAddVideoScreen()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Upload")
                }
            }
        },
        floatingActionButtonPosition = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FabPosition.End
        } else {
            FabPosition.Center
        }

    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (feedState) {
                    FeedState.Loading -> Unit
                    is FeedState.Success -> {
                        items(feedState.feed, key = { it.id }) { feedItem ->
                            val isLiked = likes.contains(feedItem.id)

                            FeedItemCardExpanded(
                                item = feedItem,
                                isLiked = isLiked,
                                onToggleLike = {
                                    onLikeChanged(feedItem.id, !isLiked)
                                },
                                onClick = {
                                    onNavigateToDetailScreen(feedItem.id)
                                },
                                modifier = Modifier.padding(horizontal = 8.dp),
                            )
                        }
                    }
                }
            }

            if (feedState == FeedState.Loading) {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> -fullHeight },
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight },
                    ) + fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        OverlayLoadingWheel(
                            modifier = Modifier.align(Alignment.Center),
                            contentDesc = "",
                        )
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.mainScreen(
    onNavigateToDetailScreen: (String) -> Unit,
    onNavigateToAddVideoScreen: () -> Unit,
    signInUser: () -> Unit,
    onNavigateToAdminLoginScreen: () -> Unit,
    showNotAdminDialog: () -> Unit,
    authState: AuthState,
) {
    composable(AppScreen.MainScreen.route) {
        val feedViewModel: FeedViewModel = hiltViewModel()
        val feedState by feedViewModel.feedState.collectAsState()
        val likes by feedViewModel.likes.collectAsState()

        LaunchedEffect(authState) {
            println("MainScreen: $authState")
            if (authState != AuthState.NOT_LOGGED_IN) {
                feedViewModel.getLikes(Firebase.auth.currentUser!!.uid)
            } else {
                feedViewModel.likesCoroutine?.cancel("User Logged Out")
            }
        }

        MainScreen(
            feedState = feedState,
            likes = likes,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            onLikeChanged = { feedId, liked ->
                Firebase.auth.currentUser?.let {
                    feedViewModel.updateLike(it.uid, feedId, liked)
                } ?: signInUser()
            },
            onNavigateToAddVideoScreen = onNavigateToAddVideoScreen,
            onNavigateToAdminLoginScreen = onNavigateToAdminLoginScreen,
            showNotAdminDialog = showNotAdminDialog,
            isAdmin = authState == AuthState.LOGGED_IN_ADMIN
        )
    }
}

fun NavController.navigateToMainScreen() {
    popBackStack("main_screen", false)
}

@DayNightPreview
@Composable
fun MainScreenPreview() {
    GardenTheme {
        MainScreen(
            feedState = FeedState.Success(
                feed = listOf(
                    FeedItem("123", "Title #1", ""),
                    FeedItem("456", "Title #2", ""),
                )
            ),
            likes = listOf("123", "789"),
            onNavigateToDetailScreen = {},
            onLikeChanged = { _, _ -> },
            onNavigateToAddVideoScreen = {},
            showNotAdminDialog = {},
            onNavigateToAdminLoginScreen = {},
            isAdmin = false
        )
    }
}
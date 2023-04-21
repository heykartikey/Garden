package com.example.garden.view.details

import android.app.Activity
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.garden.AppScreen
import com.example.garden.view.components.AppBar
import com.example.garden.view.components.LikeButton
import com.example.garden.view.components.OverlayLoadingWheel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewStateWithHTMLData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    feedItemState: FeedItemState,
    likes: List<String>,
    onNavigateToMainScreen: () -> Unit,
    onLikeChanged: (Boolean) -> Unit,
) {


//    val webViewState = rememberSaveable(stateSaver = object : Saver<Bundle, Bundle> {
//        override fun restore(value: Bundle): Bundle {
//            return value
//        }
//
//        override fun SaverScope.save(value: Bundle): Bundle {
//            return value
//        }
//    }) {
//        mutableStateOf(Bundle.EMPTY)
//    }

    val context = LocalContext.current

    Scaffold(topBar = {
        AppBar(title = "", onNavigateUp = onNavigateToMainScreen) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (feedItemState is FeedItemState.Success) {
                        val isLiked = likes.contains(feedItemState.data.id)

                        LikeButton(isLiked = isLiked, onToggleLike = {
                            onLikeChanged(!isLiked)
                        })
//                    }
//                        IconToggleButton(onCheckedChange = {
//                        }, checked = isLiked) {
//                            when (isLiked) {
//                                true -> Icon(
//                                    imageVector = Icons.Filled.Favorite,
//                                    contentDescription = null,
//                                    tint = Color.Red,
//                                )
//                                false -> Icon(
//                                    imageVector = Icons.Outlined.FavoriteBorder,
//                                    contentDescription = null
//                                )
//                            }
//                        }
                    } else {
                        IconButton(onClick = { /* Nothing */ }) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                    }
                    IconButton(onClick = {
                        if (feedItemState is FeedItemState.Success) {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, buildString {
                                    append(feedItemState.data.title)
                                    append("\n")
                                    append(feedItemState.data.description)
                                    append("\n")
                                    append(feedItemState.data.videoLink)
                                })
                                type = "text/plain"
                            }

                            context.startActivity(Intent.createChooser(sendIntent, "Share"))
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }
                }
            }
        }
    }, floatingActionButton = {
//            FloatingActionButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.De, contentDescription = )
//            }
    }) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (feedItemState) {
                FeedItemState.Loading -> Unit
                is FeedItemState.Success -> {
                    val webViewState = rememberWebViewStateWithHTMLData(
                        data = """
                            <body style="margin:0;padding:0;">
                                <iframe
                                    width="100%"
                                    height="100%"
                                    src="https://www.youtube.com/embed/2o5m1ovfl3c"
                                    title="YouTube video player"
                                    frameborder="0" 
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen>
                                </iframe>
                            </body>
                    """.trimIndent(),
                    )

                    LazyColumn {
                        item {
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        settings.javaScriptEnabled = true
                                        loadUrl("https://www.youtube.com/embed/2o5m1ovfl3c?autoplay=1")
                                    }
                                },
                                update = { webView ->
                                    webView.loadUrl("https://www.youtube.com/embed/2o5m1ovfl3c?autoplay=1")
                                }
                            )
//                            WebView(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(256.dp),
//                                client = WebViewClient
//                                state = webViewState,
//                                captureBackPresses = false,
//                                onCreated = { it.settings.javaScriptEnabled = true })
                        }
//                            Spacer(modifier = Modifier.height(256.dp))
//                            }
                        item {
                            Text(
                                text = feedItemState.data.title,
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        item {
                            Text(
                                feedItemState.data.description,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        items(feedItemState.data.images.toList(), key = { it }) {
                            Box(modifier = Modifier
                                .padding(
                                    start = 16.dp, end = 16.dp, bottom = 16.dp
                                )
                                .clickable {

                                }) {
                                Card {
                                    Box {
                                        AsyncImage(
                                            contentScale = ContentScale.FillWidth,
                                            model = it,
                                            modifier = Modifier.height(180.dp),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (feedItemState == FeedItemState.Loading) {
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

fun NavGraphBuilder.detailScreen(onNavigateUp: () -> Unit, signInUser: () -> Unit, userId: String?) {
    composable(
        route = AppScreen.DetailsScreen.route, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            nullable = false
        })
    ) { entry ->

        val detailsViewModel: DetailsViewModel = hiltViewModel()
        val feedItemState by detailsViewModel.feedItemState.collectAsState()
        val likes by detailsViewModel.likes.collectAsState()

        val feedId = requireNotNull(
            entry.arguments?.getString(
                "id"
            )
        )

        LaunchedEffect(Unit) {
            launch {
                detailsViewModel.getFeedDetails(feedId)
            }
        }

        LaunchedEffect(Unit) {
            launch {
                detailsViewModel.getLikes(userId)
            }
        }

        DetailsScreen(
            feedItemState = feedItemState,
            likes = likes,
            onNavigateToMainScreen = onNavigateUp,
            onLikeChanged = { liked ->
                Firebase.auth.currentUser?.let { user ->
                    detailsViewModel.updateLike(user.uid, feedId, liked)
                } ?: signInUser()
            },
        )
    }
}


fun NavController.navigateToDetailScreen(name: String) {
    this.navigate("details_screen/$name")
}

//object WebViewStateSaver : Saver<Bundle, Bundle> {
//    override fun restore(value: Bundle): Bundle = value
//    override fun SaverScope.save(value: Bundle): Bundle = Bundle(value)
//}
//
//@Composable
//fun rememberWebViewState(initialState: Bundle): State<Bundle> {
//    return rememberSaveable(saver = WebViewStateSaver, initialValue = initialState)
//}


//@Composable
//fun rememberWebViewState(initialState: () -> Bundle?): Bundle? {
//    val webViewSaver = remember {
//        object : Saver<Bundle?, Bundle> {
//            override fun restore(value: Bundle): Bundle = value
//            override fun SaverScope.save(value: Bundle?): Bundle? = value
//        }
//    }
//    return rememberSaveable(saver = webViewSaver) {
//
//    }
//}
//val getEmbed = { videoId: String ->
//    """
//    <html>
//    <body style="all:unset">
//    <iframe
//        width="100%"
//        height="100%"
//        src="https://www.youtube.com/embed/$videoId"
//        frameborder="0"
//        allowfullscreen>
//    </iframe>
//    </body>
//    </html>
//""".trimIndent()
//}
//
//@Composable
//fun YoutubePlayer(videoId: String, webViewState: State<Bundle>) {
//
//    val context = LocalContext.current
//
//    val webView = remember {
//        WebView(context).apply {
//            settings.javaScriptEnabled = true
//            on
//        }
//    }
//
//    LaunchedEffect(true) {
//        if (webViewState.value == Bundle.EMPTY) {
////                run {
//            webView.loadData(getEmbed(videoId), "text/html", "utf-8")
////                }
//        } else {
//            webViewState.value.let { state ->
//                webView.restoreState(state)
//            }
////            ?: run {
////        }
//        }
//    }
//
//    AndroidView(
//        { webView }, modifier = Modifier
//            .height(180.dp)
//            .fillMaxWidth(),
//
//    )
//
////    AndroidView(factory = { context ->
////        val webView = WebView(context)
////        webView.settings.cacheMode = 100
////        webView.loadData(
////            "<embed url =\"https://www.youtube.com/embed/$videoId\"></embed>",
////            " text / html ",
////            " utf -8"
////        )
////        return@AndroidView webView
////    })
//
//
////    AndroidView(
////        factory = { context ->
////            val webView = WebView(context)
////            webView.settings.javaScriptEnabled = true
////            webView.loadData(html, "text/html", "utf-8")
////            webView
////        }, modifier = Modifier
////            .height(180.dp)
////            .fillMaxWidth()
////    )
//
//}

@Composable
fun ShareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooserTitle = "Share"
    val intentChooser = Intent.createChooser(intent, chooserTitle)
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        context.startActivity(intentChooser)
        onDispose {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }
}

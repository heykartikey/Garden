package com.example.garden.view.add_video

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.garden.AppScreen
import com.example.garden.DayNightPreview
import com.example.garden.R
import com.example.garden.UploadService
import com.example.garden.domain.model.VideoDetails
import com.example.garden.ui.theme.GardenTheme
import com.example.garden.view.components.AppBar

@Composable
fun AddVideoScreen(
    title: String,
    videoLink: String,
    description: String,
    images: List<Uri>,
    setTitle: (String) -> Unit,
    setDescription: (String) -> Unit,
    setVideoLink: (String) -> Unit,
    setImages: (List<Uri>) -> Unit,
    onNavigateUp: () -> Unit,
    addVideo: (VideoDetails, List<Uri>) -> Unit,
    resetState: () -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {
            setImages(it)
        })

    DisposableEffect(Unit) {
        onDispose {
            resetState()
        }
    }

    Scaffold(
        topBar = {
            AppBar(title = "Add Video", onNavigateUp = onNavigateUp) {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { launcher.launch("image/*") }) {
                            Icon(painterResource(id = R.drawable.attach), contentDescription = null)
                        }
                        IconButton(
                            onClick = {
//                                context.startForegroundService(Intent(
//                                    context, UploadService::class.java
//                                ).apply {
//                                    putExtra("title", title)
//                                    putExtra("description", description)
//                                    putExtra("videoLink", videoLink)
//                                    putExtra("images", ArrayList<String>().apply {
//                                        addAll(images.map { it.toString() })
//                                    })
//                                })
                                addVideo(
                                    VideoDetails(
                                        title = title,
                                        description = description,
                                        videoLink = videoLink
                                    ), images
                                )
                                setTitle("")
                                setDescription("")
                                setVideoLink("")
                                setImages(listOf())

                                onNavigateUp()

                                // TODO: Do we switch to main screen and show a snack-bar?
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.secondary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onSecondary
                            )
                        }
                    }
                }
            }
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = setTitle,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent,
                        ),
                        placeholder = {
                            Text("Title")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Divider()
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedTextField(value = videoLink,
                        onValueChange = setVideoLink,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        placeholder = {
                            Text("Video Link")
                        })
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Divider()
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = setDescription,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Description")
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.None
                        ),
                    )
                }

                images.forEach {
                    item {
                        UploadImageItem(uri = it)
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun UploadImageItem(uri: Uri) {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card {
            Box {
                AsyncImage(
                    contentScale = ContentScale.FillWidth,
                    model = uri,
                    modifier = Modifier.height(180.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun UploadImageItemPreview() {
    GardenTheme {
        UploadImageItem(uri = Uri.EMPTY)
    }
}

fun NavController.navigateToAddVideoScreen() {
    navigate(AppScreen.AddVideoScreen.route)
}

fun NavGraphBuilder.addVideoScreen(
    onNavigateUp: () -> Unit,
    addVideoViewModel: AddVideoViewModel,
) {
    composable(AppScreen.AddVideoScreen.route) {
        AddVideoScreen(title = addVideoViewModel.title,
            videoLink = addVideoViewModel.videoLink,
            description = addVideoViewModel.description,
            images = addVideoViewModel.images,
            setTitle = { title -> addVideoViewModel.updateTitle(title) },
            setVideoLink = { videoLink ->
                addVideoViewModel.updateVideoLink(videoLink)
            },
            setDescription = { description ->
                addVideoViewModel.updateDescription(
                    description
                )
            },
            setImages = { images ->
                addVideoViewModel.updateImages(images)
            },
            onNavigateUp = onNavigateUp,
            addVideo = { details, images ->
                addVideoViewModel.addVideoDetails(details, images)
            },
            resetState = { addVideoViewModel.resetState() })
    }
}

@DayNightPreview
@Composable
fun AddVideoScreenPreview() {
    GardenTheme {
        AddVideoScreen(title = "",
            description = "",
            videoLink = "",
            setDescription = {},
            setTitle = {},
            setVideoLink = {},
            images = listOf<Uri>(Uri.EMPTY, Uri.EMPTY, Uri.EMPTY, Uri.EMPTY, Uri.EMPTY),
            setImages = {},
            onNavigateUp = {},
            addVideo = { _, _ -> },
            resetState = {})
    }
}
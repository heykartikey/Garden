package com.example.garden

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.garden.ui.theme.GardenTheme
import com.example.garden.view.add_video.AddVideoViewModel
import com.example.garden.view.admin_login.navigateToAdminLoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KProperty

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            applicationContext,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestProfile()
                .build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GardenTheme {
                val snackbarState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val addVideoViewModel: AddVideoViewModel = hiltViewModel()

                val authStateViewModel by viewModels<FirebaseAuthStateViewModel>()
                val authState by authStateViewModel.authState.collectAsState()

                val showMessage = { message: String ->
                    coroutineScope.launch {
                        snackbarState.showSnackbar(message, null, SnackbarDuration.Short)
                    }
                    Unit
                }

                val authLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == RESULT_OK) {
                            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                            try {
                                val account = task.getResult(ApiException::class.java)
                                val credential =
                                    GoogleAuthProvider.getCredential(account.idToken, null)

                                coroutineScope.launch {
                                    Firebase.auth.signInWithCredential(credential).await()
                                    snackbarState.showSnackbar(
                                        "You are logged in now", null, SnackbarDuration.Short
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                if (e is CancellationException) throw e // TODO: Why throw here (or is it not necessary)?
                            }
                        } else {
                            showMessage("Couldn't sign in")
                        }
                    }

                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing
                        )
                ) {
                    val navController = rememberNavController()

                    var alertNotAdminDialogVisible by remember {
                        mutableStateOf(false)
                    }

                    val hideNotAdminDialog = {
                        alertNotAdminDialogVisible = false
                    }

                    val showNotAdminDialog = {
                        alertNotAdminDialogVisible = true
                    }

                    Box {
                        Navigation(
                            addVideoViewModel = addVideoViewModel,
                            signInUser = {
                                authLauncher.launch(googleSignInClient.signInIntent)
                            },
                            navController = navController,
                            showNotAdminDialog = showNotAdminDialog,
                            authState = authState
                        )

                        SnackbarHost(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 72.dp), hostState = snackbarState
                        )
                    }

                    AlertNotAdminDialog(
                        visible = alertNotAdminDialogVisible,
                        hideDialog = hideNotAdminDialog
                    ) {
                        hideNotAdminDialog()
                        navController.navigateToAdminLoginScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun AlertNotAdminDialog(visible: Boolean, hideDialog: () -> Unit, onSignInClick: () -> Unit) {
    AnimatedVisibility(visible = visible) {
        Dialog(onDismissRequest = hideDialog) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface {
                        Text(text = "You are not an admin", style = MaterialTheme.typography.h5)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        TextButton(onClick = hideDialog) {
                            Text("Cancel")
                        }
                        Button(onClick = onSignInClick) {
                            Text("Sign in as Admin")
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetPreview() {
    GardenTheme {
        AlertNotAdminDialog(onSignInClick = {}, visible = true, hideDialog = {})
    }
}


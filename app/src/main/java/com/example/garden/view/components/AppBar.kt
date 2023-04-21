package com.example.garden.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean = true,
    onNavigateUp: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) {
    TopAppBar(
        elevation = 0.dp, backgroundColor = MaterialTheme.colors.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(text = title, style = MaterialTheme.typography.h5)
            }

            if (showBackButton) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = "Go Back"
                        )
                    }
                }
            }

            content()
        }
    }
}
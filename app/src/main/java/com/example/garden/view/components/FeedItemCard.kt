package com.example.garden.view.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.garden.ui.theme.GardenTheme
import com.example.garden.view.main.FeedItem

private object LikeButtonRipple : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        Color.Red, lightTheme = true
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Black, lightTheme = true
    )
}

@Composable
fun LikeButton(
    isLiked: Boolean,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalRippleTheme provides LikeButtonRipple) {
        IconToggleButton(
            checked = isLiked, onCheckedChange = { onToggleLike() }, modifier = modifier

                .clip(CircleShape)
                .background(
                    when (isLiked) {
                        true -> LocalRippleTheme.current
                            .defaultColor()
                            .copy(alpha = LocalRippleTheme.current.rippleAlpha().pressedAlpha)
                        false -> Color.Transparent
                    }
                )
        ) {
            when (isLiked) {
                true -> Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                )
                false -> Icon(
                    imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null
                )
            }
        }
    }
}

@Preview("NotLiked")
@Composable
fun LikeButtonNotLikedPreview() {
    GardenTheme {
        LikeButton(isLiked = false, onToggleLike = {})
    }
}

@Preview("Liked")
@Composable
fun LikeButtonLikedPreview() {
    GardenTheme {
        LikeButton(isLiked = true, onToggleLike = {})
    }
}

@Composable
fun FeedItemCardExpanded(
    item: FeedItem, // TODO: update type
    isLiked: Boolean,
    onToggleLike: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier
    ) {
        Column(modifier = Modifier.clickable {
            onClick(); println("Card Clicked")
        }) {

            FeedItemHeaderImage(item.thumb)
//            Box(
//                modifier = Modifier.padding(16.dp),
//            ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                    Column {
//                        Spacer(modifier = Modifier.height(12.dp))
                FeedItemTitle(
                    item.title,
                    modifier = Modifier.fillMaxWidth((.8f)),
                )
//                    }
                LikeButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    isLiked = isLiked,
                    onToggleLike = onToggleLike
                )
            }
//            }
        }
    }
}

@Composable
fun FeedItemHeaderImage(
    headerImageUrl: String,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentScale = ContentScale.Crop,
        model = headerImageUrl,
        contentDescription = null, // decorative image
    )
}

@Composable
fun FeedItemTitle(
    feedItemTitle: String,
    modifier: Modifier = Modifier,
) {
    Text(feedItemTitle, style = MaterialTheme.typography.h3, modifier = modifier)
}

@Preview(showBackground = false)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = false)
@Composable
private fun ExpandedNewsResourcePreview(
) {
    GardenTheme {
        FeedItemCardExpanded(
            item = FeedItem("123", "Title", ""),
            isLiked = true,
            onToggleLike = {},
            onClick = {},
        )
    }
}

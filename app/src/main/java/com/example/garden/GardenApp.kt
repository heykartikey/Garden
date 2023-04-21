package com.example.garden

import android.app.Application
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GardenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        println("awefihwa")
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class DayNightPreview
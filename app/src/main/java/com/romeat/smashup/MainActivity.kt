package com.romeat.smashup

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.romeat.smashup.navgraphs.RootNavigationGraph
import com.romeat.smashup.ui.theme.SmashupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val activity = LocalContext.current as Activity
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            SmashupTheme(darkTheme = true) {
                RootNavigationGraph(navController = rememberAnimatedNavController())
            }
        }
    }
}
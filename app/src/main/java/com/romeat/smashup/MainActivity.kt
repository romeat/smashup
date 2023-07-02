package com.romeat.smashup

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.romeat.smashup.navgraphs.RootNavigationGraph
import com.romeat.smashup.ui.theme.SmashupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    val TAG = "MainActivity"
    lateinit var navController: NavHostController

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val activity = LocalContext.current as Activity
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            navController = rememberAnimatedNavController()
            SmashupTheme(darkTheme = true) {
                RootNavigationGraph(navController = navController)
                checkIntent(intent)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { checkIntent(it) }
    }

    private fun checkIntent(intent: Intent) {
        if (intent.action == ACTION_VIEW) {
            val action: String? = intent.action
            val data: Uri? = intent.data
            val eae = intent.data?.pathSegments
            Log.d(TAG, "checkIntent: action: $action data: $data scheme: $eae")
            Log.d(TAG, "checkIntent: ${viewModel.state.value == null}")
            if (viewModel.state.value == null) {
                navController.handleDeepLink(intent)
            }
        }
    }
}
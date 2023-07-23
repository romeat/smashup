package com.romeat.smashup

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.romeat.smashup.navgraphs.RootGraph
import com.romeat.smashup.navgraphs.RootNavigationGraph
import com.romeat.smashup.ui.theme.SmashupTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        collectEvents()
        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, token)
        })
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

            val authDeeplinkPaths = listOf(
                getString(R.string.deeplink_path_register_confirm),
                getString(R.string.deeplink_path_password_recover_confirm),
            )

            val homeDeeplinkPaths = listOf(
                getString(R.string.deeplink_path_change_email_confirm),
            )

            if (viewModel.isUserLogged()) {
                // todo handle deeplinks for logged users
                return
            } else {
                // Auth navgraph deeplinks
                intent.data?.path?.let { incomingDeeplinkPath ->
                    if (authDeeplinkPaths.any { authPath ->
                            incomingDeeplinkPath.contains(authPath)
                        }
                    ) {
                        navController.handleDeepLink(intent)
                    }
                }
            }
        }
    }

    private fun collectEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is MainEvent.NavigateToAuth -> {
                            navController.navigate(RootGraph.AUTHENTICATION) { popUpTo(RootGraph.ROOT) }
                        }
                    }
                }
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            //
        }
    }
}
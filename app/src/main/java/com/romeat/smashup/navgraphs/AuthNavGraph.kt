package com.romeat.smashup.navgraphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.romeat.smashup.presentation.login.greetings.GreetingsScreen
import com.romeat.smashup.presentation.login.password.EmailRecoverySentScreen
import com.romeat.smashup.presentation.login.password.ForgotPasswordScreen
import com.romeat.smashup.presentation.login.password.PasswordUpdatedScreen
import com.romeat.smashup.presentation.login.password.SetNewPasswordScreen
import com.romeat.smashup.presentation.login.register.RegisterScreen
import com.romeat.smashup.presentation.login.signin.SignInScreen
import com.romeat.smashup.util.AuthNavigationConstants
import com.romeat.smashup.util.CommonNavigationConstants

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthScreen.Greetings.route,
        route = RootGraph.AUTHENTICATION
    ) {

        val regConfirmDeepLink = "https://smashup.ru/register/confirm?id={id}"
        val recoverPasswordDeepLink = "https://smashup.ru/user/recover_password/confirm?id={id}"

        composable(route = AuthScreen.Greetings.route) {
            GreetingsScreen(
                onRegistrationClick = {
                    navController.navigate(AuthScreen.Register.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                },
                onLoginClick = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                }
            )
        }
        composable(
            route = AuthScreen.Register.route,
            deepLinks = listOf(navDeepLink { uriPattern = "https://smashup.ru/register_confirm?id={id}" })
        ) { backStackEntry ->
            RegisterScreen(
                toSignInScreen = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                },
                toRegisterConfirm = {
                    // todo
                }
            )
        }
        composable(route = AuthScreen.SignIn.route) {
            SignInScreen(
                toHomeScreen = {
                    navController.popBackStack()
                    navController.navigate(RootGraph.HOME)
                },
                toRegister = {
                    navController.navigate(AuthScreen.Register.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                },
                toForgotPassword = {
                    navController.navigate(AuthScreen.ForgotPassword.route)
                },
            )
        }
        composable(route = AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                toSuccessScreen = { email ->
                    navController.navigate("${AuthScreen.EmailRecoverySent.route}/${email}") {
                        popUpTo(AuthScreen.SignIn.route)
                    }
                }
            )
        }
        composable(
            route = "${AuthScreen.EmailRecoverySent.route}/{${AuthNavigationConstants.EMAIL_PARAM}}",
            arguments = listOf(navArgument(AuthNavigationConstants.EMAIL_PARAM) {
                type = NavType.StringType
            }),
        ) { backStackEntry ->
            EmailRecoverySentScreen(
                email = backStackEntry.arguments?.getString(AuthNavigationConstants.EMAIL_PARAM)!!,
                onConfirmClick = {
                    navController.navigate(AuthScreen.Greetings.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                }
            )
        }
        composable(
            route = AuthScreen.SetNewPassword.route,
            deepLinks = listOf(navDeepLink { uriPattern = recoverPasswordDeepLink })
        ) {
            SetNewPasswordScreen(
                onBackClick = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                },
                toSuccessScreen = {
                    navController.navigate(AuthScreen.PasswordUpdated.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                }
            )
        }
        composable(
            route = AuthScreen.PasswordUpdated.route,
        ) {
            PasswordUpdatedScreen(
                onConfirmClick = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(RootGraph.AUTHENTICATION)
                    }
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {

    object Greetings : AuthScreen(route = "GREETINGS")
    object SignIn : AuthScreen(route = "SIGN_IN")
    object Register : AuthScreen(route = "REGISTER")
    object ForgotPassword : AuthScreen(route = "FORGOT_PASSWORD")
    object EmailRecoverySent : AuthScreen(route = "EMAIL_RECOVERY_SENT")
    object SetNewPassword : AuthScreen(route = "SET_NEW_PASSWORD")
    object PasswordUpdated : AuthScreen(route = "PASSWORD_UPDATED")
}
package com.romeat.smashup.navgraphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navigation
import com.romeat.smashup.presentation.login.greetings.GreetingsScreen
import com.romeat.smashup.presentation.login.register.RegisterScreen
import com.romeat.smashup.presentation.login.signin.SignInScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthScreen.Greetings.route,
        route = RootGraph.AUTHENTICATION
    ) {
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
                toForgotPassword = { /*TODO*/ },
            )
        }
    }
}

sealed class AuthScreen(val route: String) {

    object Greetings : AuthScreen(route = "GREETINGS")
    object SignIn : AuthScreen(route = "SIGN_IN")
    object Register : AuthScreen(route = "REGISTER")
    object ForgotPassword : AuthScreen(route = "FORGOT_PASSWORD")
}
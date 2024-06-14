package com.simplecode01.assessment3.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplecode01.assessment3.ui.screen.AboutAppsScreen
import com.simplecode01.assessment3.ui.screen.AddItemScreen
import com.simplecode01.assessment3.ui.screen.KEY_ID_USER
import com.simplecode01.assessment3.ui.screen.ListItemScreen
import com.simplecode01.assessment3.ui.screen.LoginScreen
import com.simplecode01.assessment3.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    )
    {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.AboutApp.route) {
            AboutAppsScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(
            route = Screen.List.route,
            arguments = listOf(
                navArgument(KEY_ID_USER) { type = NavType.StringType }
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(KEY_ID_USER)
            ListItemScreen(navController, id)
        }
        composable(route = Screen.Add.route) {
            AddItemScreen(navController)
        }
    }
}
package com.example.deucapstone2023.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.HOME.route
    ) {
        composable(NavigationItem.HOME.route) {

        }

        composable(NavigationItem.LOGIN.route) {

        }

        composable(NavigationItem.SETTING.route) {

        }
    }
}
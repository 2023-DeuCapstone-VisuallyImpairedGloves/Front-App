package com.example.deucapstone2023.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deucapstone2023.ui.screen.home.Home
import com.example.deucapstone2023.ui.screen.home.HomeViewModel
import com.example.deucapstone2023.ui.screen.home.search.SearchViewModel
import com.skt.tmap.TMapView

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    tMapView: TMapView,
    homeViewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.HOME.route,
        modifier = modifier
    ) {
        composable(route = NavigationItem.HOME.route) {
            val uiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
            Home(
                tMapView = tMapView,
                title = uiState.title,
                onTitleChanged = homeViewModel::setTitle,
                onNavigateToNaviScreen = {}
            )
        }

        composable(route = NavigationItem.SEARCH.route) {

        }

        composable(NavigationItem.SETTING.route) {

        }
    }
}

@Composable
fun BottomNavigationGraph(
    navController: NavHostController
) {
    val clickState = remember {
        mutableStateOf(false)
    }
    val items = listOf(
        NavigationItem.HOME,
        NavigationItem.SETTING
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val backStackEntry = navController.currentBackStackEntryAsState()
        items.forEach { screen ->
            val selected = backStackEntry.value?.destination?.route == screen.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(
                            id =
                            if (!selected)
                                screen.icon!!
                            else
                                screen.iconClicked!!
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    clickState.value = !clickState.value
                }
            )
        }
    }

}
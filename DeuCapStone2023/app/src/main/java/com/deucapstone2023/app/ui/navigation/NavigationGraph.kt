package com.deucapstone2023.app.ui.navigation

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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.deucapstone2023.app.ui.base.CommonRecognitionListener
import com.deucapstone2023.app.ui.screen.list.ListScreen
import com.deucapstone2023.app.ui.screen.search.HomeScreen
import com.deucapstone2023.app.ui.screen.search.SearchViewModel
import com.deucapstone2023.app.ui.screen.setting.SettingScreen
import com.deucapstone2023.app.ui.screen.setting.SettingViewModel
import com.deucapstone2023.app.ui.screen.temp.HomeViewModel


@Composable
fun NavigationGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel,
    settingViewModel: SettingViewModel,
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.SEARCH.route
    ) {
        composable(route = NavigationItem.SEARCH.route) {
            HomeScreen(
                searchViewModel = searchViewModel,
                homeViewModel = homeViewModel,
                startListening = startListening,
                checkIsSpeaking = checkIsSpeaking,
                voiceOutput = voiceOutput,
                setSpeechRecognizerListener = setSpeechRecognizerListener
            )
        }

        composable(route = NavigationItem.SETTING.route) {
            SettingScreen(
                settingViewModel = settingViewModel
            )
        }

        composable(route = NavigationItem.LIST.route) {
            ListScreen()
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
        NavigationItem.SEARCH,
        NavigationItem.SETTING,
        NavigationItem.LIST
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        items.filter { it.icon != null }.forEach { screen ->
            val selected = navBackStackEntry?.destination?.route == screen.route
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
                selected = selected,
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
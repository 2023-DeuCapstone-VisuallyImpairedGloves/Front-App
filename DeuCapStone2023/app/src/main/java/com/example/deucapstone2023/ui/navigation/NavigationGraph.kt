package com.example.deucapstone2023.ui.navigation

import android.bluetooth.BluetoothManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.screen.search.HomeScreen
import com.example.deucapstone2023.ui.screen.search.HomeViewModel
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.search.SearchEventFlow
import com.example.deucapstone2023.ui.screen.search.SearchViewModel
import com.example.deucapstone2023.ui.screen.setting.SettingScreen
import com.example.deucapstone2023.ui.screen.setting.SettingViewModel
import com.example.deucapstone2023.ui.screen.setting.state.ButtonStatus
import com.skt.tmap.TMapView


@Composable
fun NavigationGraph(
    tMapView: TMapView,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel,
    settingViewModel: SettingViewModel = hiltViewModel(),
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit,
    setUpBluetooth: (() -> Unit, () -> Unit) -> Unit,
    disableBluetooth: (() -> Unit, () -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.SEARCH.route
    ) {
        composable(route = NavigationItem.SEARCH.route) {
            val searchEventFlow by searchViewModel.searchEventFlow.collectAsStateWithLifecycle(
                initialValue = SearchEventFlow.Loading,
                lifecycleOwner = LocalLifecycleOwner.current,
                minActiveState = Lifecycle.State.STARTED
            )
            val searchUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

            HomeScreen(
                searchEventFlow = searchEventFlow,
                searchUiState = searchUiState,
                tMapView = tMapView,
                startListening = startListening,
                checkIsSpeaking = checkIsSpeaking,
                voiceOutput = voiceOutput,
                makeMarker = searchViewModel::makeMarker,
                getRoutePedestrian = searchViewModel::getRoutePedestrian,
                setSpeechRecognizerListener = setSpeechRecognizerListener,
                setDestinationInfo = searchViewModel::setDestinationInfo,
                navigateRouteOnMap = searchViewModel::navigateRouteOnMap,
                title = homeUiState.title,
                onTitleChanged = homeViewModel::setTitle,
                onNavigateToNaviScreen = {
                    searchViewModel::searchPlaceOnTyping.invoke(
                        context.getString(R.string.T_Map_key),
                        homeUiState.title
                    )
                },
            )
        }

        composable(route = NavigationItem.SETTING.route) {
            val settingUiState by settingViewModel.settingUiState.collectAsStateWithLifecycle()

            SettingScreen(
                settingUiState = settingUiState,
                setControlStatus = settingViewModel::setControlStatus,
                setUpBluetooth = setUpBluetooth,
                disableBluetooth = disableBluetooth
            )
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
        NavigationItem.SETTING
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
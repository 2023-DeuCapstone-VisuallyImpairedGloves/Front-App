package com.deucapstone2023.ui.screen.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deucapstone2023.design.R
import com.deucapstone2023.design.component.DefaultLayout
import com.deucapstone2023.design.component.DropDownMenuCustom
import com.deucapstone2023.design.component.VerticalSpacer
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.design.utils.tu
import com.deucapstone2023.ui.screen.list.component.SensorInfoHeader
import com.deucapstone2023.ui.screen.list.component.SensorInfoItems
import com.deucapstone2023.ui.screen.list.state.MenuItem
import com.deucapstone2023.ui.screen.list.state.UserLocation
import com.deucapstone2023.ui.screen.list.state.findMenuFromString

@Composable
fun ListScreen(
    listViewModel: ListViewModel = hiltViewModel()
) {
    val uiState by listViewModel.uiState.collectAsStateWithLifecycle()
    ListScreen(
        listUiState = uiState,
        setMenuTextChanged = { menuName -> listViewModel::setMenu.invoke(menuName.findMenuFromString()!!) }
    )
}

@Composable
private fun ListScreen(
    listUiState: ListUiState,
    setMenuTextChanged: (String) -> Unit
) {
    DefaultLayout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            DropDownMenuCustom(
                iconTail = R.drawable.ic_arrow_down_small,
                text = listUiState.menu.desc,
                items = MenuItem.values().map { it.desc },
                setTextChanged = setMenuTextChanged
            )
            VerticalSpacer(height = 20.dp)
            LazyColumn {
                when (listUiState.menu) {
                    MenuItem.USERLOCATION -> {
                        item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "날짜",
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                                Text(
                                    text = "장소",
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                                Text(
                                    text = "출발지",
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                                Text(
                                    text = "목적지",
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                            }
                        }
                        itemsIndexed(
                            items = listUiState.userLocationList,
                            key = { _, item -> item.id }
                        ) { _, item ->
                            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = item.date,
                                    maxLines = 1,
                                    fontSize = 12.tu,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )

                                Text(
                                    text = item.nearPoiName,
                                    maxLines = 1,
                                    fontSize = 12.tu,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )

                                Text(
                                    text = item.source,
                                    maxLines = 1,
                                    fontSize = 12.tu,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )

                                Text(
                                    text = item.dest,
                                    maxLines = 1,
                                    fontSize = 12.tu,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                            }
                        }
                    }

                    MenuItem.AZIMUTH -> {
                        item {
                            SensorInfoHeader()
                        }
                        itemsIndexed(
                            items = listUiState.azimuthSensorList,
                            key = { index, item -> item.id }
                        ) { index, item ->
                            SensorInfoItems(item = item)
                        }
                    }

                    MenuItem.DISTANCE -> {
                        item {
                            SensorInfoHeader()
                        }
                        itemsIndexed(
                            items = listUiState.distanceSensorList,
                            key = { index, item -> item.id }
                        ) { index, item ->
                            SensorInfoItems(item = item)
                        }
                    }

                    MenuItem.OBSTACLE -> {
                        item {
                            SensorInfoHeader()
                        }
                        itemsIndexed(
                            items = listUiState.obstacleSensorList,
                            key = { index, item -> item.id }
                        ) { index, item ->
                            SensorInfoItems(item = item)
                        }
                    }
                }

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ListScreenPreview() =
    DeuCapStone2023Theme {
        ListScreen(
            listUiState = ListUiState(
                userLocationList = listOf(
                    UserLocation(
                        latitude = 0.0,
                        longitude = 0.0,
                        nearPoiName = "동의대학교 정문",
                        source = "동의대학교 공과대학",
                        dest = "봉구스 동의대점",
                        date = "2023/05/24",
                        id = 0
                    ),
                    UserLocation(
                        latitude = 0.0,
                        longitude = 0.0,
                        nearPoiName = "동의대학교 정문",
                        source = "동의대학교 공과대학",
                        dest = "봉구스 동의대점",
                        date = "2023/05/24",
                        id = 1
                    ),
                    UserLocation(
                        latitude = 0.0,
                        longitude = 0.0,
                        nearPoiName = "동의대학교 정문",
                        source = "동의대학교 공과대학",
                        dest = "봉구스 동의대점",
                        date = "2023/05/24",
                        id = 2
                    ),
                    UserLocation(
                        latitude = 0.0,
                        longitude = 0.0,
                        nearPoiName = "동의대학교 정문",
                        source = "동의대학교 공과대학",
                        dest = "봉구스 동의대점",
                        date = "2023/05/24",
                        id = 3
                    )
                ),
                azimuthSensorList = emptyList(),
                distanceSensorList = emptyList(),
                obstacleSensorList = emptyList(),
                menu = MenuItem.USERLOCATION
            ),
            setMenuTextChanged = {}
        )
    }
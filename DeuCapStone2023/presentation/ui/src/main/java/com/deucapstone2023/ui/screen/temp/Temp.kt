package com.deucapstone2023.ui.screen.temp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.deucapstone2023.design.component.DefaultLayout
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.ui.screen.search.state.POIState
import com.deucapstone2023.ui.screen.temp.component.SearchAppBar
import com.deucapstone2023.ui.screen.temp.component.SearchList

@Composable
fun SearchScreen(
    poiList: List<POIState>,
    title: String,
    onTitleChanged: (String) -> Unit,
    onNavigateToHome: () -> Unit
) {
    DefaultLayout(
        topBar = {
            SearchAppBar(
                title = title,
                hintMessage = "장소, 버스, 지하철, 주소 검색",
                onTitleChanged = onTitleChanged,
                onNavigateToHome = onNavigateToHome
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        SearchList(
            poiItems = poiList,
            contentPadding = it
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSearchScreen() =
    DeuCapStone2023Theme {
        SearchScreen(
            poiList =
            listOf(
                POIState(
                    id = 0L,
                    name = "당감댁",
                    address = "부산 부산진구 냉정로 239 1층",
                    distance = "0.1km",
                    biz = "닭볶음탕",
                    latitude = .0,
                    longitude = .0
                ),
                POIState(
                    id = 0L,
                    name = "당감댁",
                    address = "부산 부산진구 냉정로 239 1층",
                    distance = "0.1km",
                    biz = "닭볶음탕",
                    latitude = .0,
                    longitude = .0
                ),
                POIState(
                    id = 0L,
                    name = "당감댁",
                    address = "부산 부산진구 냉정로 239 1층",
                    distance = "0.1km",
                    biz = "닭볶음탕",
                    latitude = .0,
                    longitude = .0
                ),
                POIState(
                    id = 0L,
                    name = "당감댁",
                    address = "부산 부산진구 냉정로 239 1층",
                    distance = "0.1km",
                    biz = "닭볶음탕",
                    latitude = .0,
                    longitude = .0
                )
            ),
            title = "",
            onTitleChanged = {},
            onNavigateToHome = {}
        )
    }
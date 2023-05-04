package com.example.deucapstone2023.ui.screen.home.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.screen.home.search.component.SearchAppBar
import com.example.deucapstone2023.ui.screen.home.search.component.SearchList
import com.example.deucapstone2023.ui.screen.home.search.state.POIState
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme

@Composable
fun Search(
    searchUiState: SearchUiState,
    title: String,
    onTitleChanged: (String) -> Unit
) {
    DefaultLayout(
        topBar = {
            SearchAppBar(
                title = title,
                hintMessage = "장소, 버스, 지하철, 주소 검색",
                onTitleChanged = onTitleChanged,
                onNavigateClick = {}
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        SearchList(
            poiItems = searchUiState.poiList,
            contentPadding = it
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSearchScreen() =
    DeuCapStone2023Theme {
        Search(
            searchUiState = SearchUiState(
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
                latitude = .0,
                longitude = .0,
                routeList = emptyList()
            ),
            title = "",
            onTitleChanged = {}
        )
    }
package com.example.deucapstone2023.ui.screen.home.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.component.HorizontalDivider
import com.example.deucapstone2023.ui.component.HorizontalSpacer
import com.example.deucapstone2023.ui.component.VerticalSpacer
import com.example.deucapstone2023.ui.screen.home.search.POIState
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.black
import com.example.deucapstone2023.ui.theme.blue
import com.example.deucapstone2023.ui.theme.deepGray
import com.example.deucapstone2023.utils.tu

@Composable
fun SearchList(
    poiItems: List<POIState>,
    contentPadding: PaddingValues
) {
    LazyColumn(modifier = Modifier.padding(contentPadding)) {
        itemsIndexed(
            items = poiItems,
            key = null
        ) { index, item ->
            SearchItem(
                poiName = item.poiName,
                poiAddress = item.poiAddress,
                poiDistance = item.poiDistance,
                poiBiz = item.poiBiz
            )
            if (index != poiItems.lastIndex)
                HorizontalDivider()
        }

    }
}

@Composable
private fun SearchItem(
    poiName: String,
    poiAddress: String,
    poiDistance: String,
    poiBiz: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = "locationIcon"
        )
        HorizontalSpacer(width = 8.dp)
        Column() {
            Text(
                text = poiName,
                fontSize = 14.tu,
                fontWeight = FontWeight.Bold,
                color = blue
            )
            VerticalSpacer(height = 4.dp)
            Text(
                text = poiAddress,
                fontSize = 12.tu,
                fontWeight = FontWeight.Bold,
                color = deepGray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column() {
            Text(
                text = poiBiz,
                fontSize = 12.tu,
                fontWeight = FontWeight.Bold,
                color = black
            )
            VerticalSpacer(height = 4.dp)
            Text(
                text = poiDistance,
                fontSize = 10.tu,
                fontWeight = FontWeight.Bold,
                color = black
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSearchList() =
    DeuCapStone2023Theme {
        SearchList(
            poiItems = listOf(
                POIState(
                    poiName = "당감댁",
                    poiAddress = "부산 부산진구 냉정로 239 1층",
                    poiDistance = "0.1km",
                    poiBiz = "닭볶음탕"
                ),
                POIState(
                    poiName = "당감댁",
                    poiAddress = "부산 부산진구 냉정로 239 1층",
                    poiDistance = "0.1km",
                    poiBiz = "닭볶음탕"
                ),
                POIState(
                    poiName = "당감댁",
                    poiAddress = "부산 부산진구 냉정로 239 1층",
                    poiDistance = "0.1km",
                    poiBiz = "닭볶음탕"
                ),
                POIState(
                    poiName = "당감댁",
                    poiAddress = "부산 부산진구 냉정로 239 1층",
                    poiDistance = "0.1km",
                    poiBiz = "닭볶음탕"
                )
            ),
            contentPadding = PaddingValues()
        )
    }

@Composable
@Preview(showBackground = true)
private fun PreviewSearchItem() =
    DeuCapStone2023Theme {
        SearchItem(
            poiName = "당감댁",
            poiAddress = "부산 부산진구 냉정로 239 1층",
            poiDistance = "0.1km",
            poiBiz = "닭볶음탕"
        )
    }


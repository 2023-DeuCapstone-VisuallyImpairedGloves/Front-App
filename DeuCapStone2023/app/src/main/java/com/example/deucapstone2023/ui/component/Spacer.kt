package com.example.deucapstone2023.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacer(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier.height(height))
}

@Suppress("FunctionName")
fun LazyListScope.VerticalSpacerItem(height: Dp) {
    item { VerticalSpacer(height) }
}

@Composable
fun HorizontalSpacer(
    width: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier.width(width))
}

@Suppress("FunctionName")
fun LazyListScope.HorizontalSpacerItem(width: Dp) {
    item { HorizontalSpacer(width) }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun VerticalSpacerPreview() {
    Column {
        Box(Modifier.fillMaxWidth().weight(1f).background(Color.White))
        VerticalSpacer(20.dp)
        Box(Modifier.fillMaxWidth().weight(1f).background(Color.White))
    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun HorizontalSpacerPreview() {
    Row {
        Box(Modifier.fillMaxHeight().weight(1f).background(Color.White))
        HorizontalSpacer(20.dp)
        Box(Modifier.fillMaxHeight().weight(1f).background(Color.White))
    }
}
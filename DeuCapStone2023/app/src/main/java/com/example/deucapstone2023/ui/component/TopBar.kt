package com.example.deucapstone2023.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.white

@Composable
fun DefaultAppBar(
    @DrawableRes headerIcon: Int? = null,
    modifier: Modifier = Modifier,
    onHeaderIconClick: () -> Unit = {},
    content: @Composable () -> Unit,
    tailIcon: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .background(shape = RoundedCornerShape(8.dp), color = white)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        headerIcon?.let {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = onHeaderIconClick,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 18.dp)
                        )
                        .size(24.dp),
                    painter = painterResource(id = headerIcon),
                    contentDescription = "back"
                )
            }
        }

        HorizontalSpacer(width = 4.dp)
        content()
        Spacer(modifier = Modifier.weight(1f))
        tailIcon()
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDefaultAppBar() =
    DeuCapStone2023Theme {
        DefaultAppBar(
            headerIcon = R.drawable.ic_hamburger,
            onHeaderIconClick = {},
            content = {},
            tailIcon = {}
        )
    }
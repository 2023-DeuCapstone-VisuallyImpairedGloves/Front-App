package com.deucapstone2023.design.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Stable
val Int.tu: TextUnit
    @Composable
    get() = with(LocalDensity.current) { (this@tu / fontScale).sp }
package com.example.deucapstone2023.ui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.deucapstone2023.ui.navigation.NavigationGraph
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeuCapStone2023Theme {
                this.Content()
            }
        }
    }

    @Composable
    private fun Content() {
        NavigationGraph(
            navController = rememberNavController()
        )
    }
}
package com.fearlauncher

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import com.fearlauncher.ui.components.BottomNavBar
import com.fearlauncher.ui.screens.HomeScreen
import com.fearlauncher.ui.screens.PlayScreen
import com.fearlauncher.ui.screens.SettingsScreen

@Composable
fun FearLauncherApp() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> HomeScreen()
            1 -> PlayScreen()
            2 -> SettingsScreen()
        }
    }
}

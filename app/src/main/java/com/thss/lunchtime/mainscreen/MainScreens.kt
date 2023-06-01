package com.thss.lunchtime.mainscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainScreens(val route: String, val name: String, val iconSelected: ImageVector, val iconUnSelected: ImageVector) {
    object Home: MainScreens("home", "主页", Icons.Filled.Home, Icons.Outlined.Home)
    object Message: MainScreens("message", "通知", Icons.Default.Notifications, Icons.Outlined.Notifications)
    object My: MainScreens("my", "我的", Icons.Default.Person, Icons.Outlined.Person)
}

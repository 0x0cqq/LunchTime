package com.thss.lunchtime.mainscreen


import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thss.lunchtime.mainscreen.homepage.Homepage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


// https://stackoverflow.com/questions/66703448/how-to-disable-ripple-effect-when-clicking-in-jetpack-compose
class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNewPost: () -> Unit, mainScreenViewModel: MainScreenViewModel) {
    val mainScreenNavController = rememberNavController()
    // 脚手架，上面下面的栏和
    val navigationBarItems = listOf(
        MainScreens.Home, MainScreens.Message, MainScreens.My
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(75.dp)
            ) {
                navigationBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = mainScreenViewModel.selectItem == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor =  MaterialTheme.colorScheme.secondary.copy(0.4f),
                        ),
                        onClick =
                        {
                            mainScreenViewModel.selectNavItem(index)
                            mainScreenNavController.navigate(item.route) {
                                popUpTo(mainScreenNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            if ( mainScreenViewModel.selectItem == index)
                                Icon(item.iconSelected, contentDescription = item.name)
                            else
                                Icon(item.iconUnSelected, contentDescription = item.name)
                        },
                        interactionSource = NoRippleInteractionSource(),
                        label = { Text(item.name) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = mainScreenNavController,
            startDestination = MainScreens.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainScreens.Home.route) {
                Homepage(onClickNewPost = onNewPost)
            }
            composable(MainScreens.Message.route) {
                MessagePage()
            }
            composable(MainScreens.My.route) {
                MyPage()
            }
        }
    }
}


@Composable
fun MessagePage() {
    Text("Message page")
}

@Composable
fun MyPage() {
    Text("My page")
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen({}, MainScreenViewModel())
}
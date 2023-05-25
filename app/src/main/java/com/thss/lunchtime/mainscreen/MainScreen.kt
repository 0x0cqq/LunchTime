package com.thss.lunchtime.mainscreen


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thss.lunchtime.common.NoRippleInteractionSource
import com.thss.lunchtime.mainscreen.infopage.MyInfoPage
import com.thss.lunchtime.mainscreen.homepage.Homepage
import com.thss.lunchtime.mainscreen.homepage.HomepageViewModel
import com.thss.lunchtime.mainscreen.infopage.MyInfoPageViewModel
import com.thss.lunchtime.mainscreen.messagepage.MessageViewModel
import com.thss.lunchtime.mainscreen.messagepage.Messagepage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onOpenInfoEdit: () -> Unit, onNewPost: () -> Unit, onOpenPost: (postId: Int) -> Unit, onOpenUserInfo: (userName: String) -> Unit, onOpenFollows: () -> Unit, onOpenFans: () -> Unit, onOpenSaved: () -> Unit, mainScreenViewModel: MainScreenViewModel) {
    val mainScreenNavController = rememberNavController()
    // 脚手架，上面下面的栏和
    val navigationBarItems = listOf(
        MainScreens.Home, MainScreens.Message, MainScreens.My
    )
    val homepageViewModel: HomepageViewModel = viewModel()
    val messageViewModel: MessageViewModel = viewModel()
    val myInfoPageViewModel: MyInfoPageViewModel = viewModel()

    LaunchedEffect(Unit) {
        val index = mainScreenViewModel.selectItem
        val item = navigationBarItems[index]
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
    }

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
                Homepage(
                    onClickPostPreviewCard = onOpenPost,
                    onClickNewPost = onNewPost,
                    homepageViewModel = homepageViewModel,
                    onOpenUserInfo = onOpenUserInfo,
                )
            }
            composable(MainScreens.Message.route) {
                Messagepage(
                    onClickPostNotice = onOpenPost,
                    messageViewModel = messageViewModel
                )
            }
            composable(MainScreens.My.route) {
                MyInfoPage(
                    onOpenInfoEdit = onOpenInfoEdit,
                    myInfoPageViewModel = myInfoPageViewModel,
                    onClickPost = onOpenPost,
                    onOpenFollowingList = onOpenFollows,
                    onOpenFansList = onOpenFans,
                    onOpenSavedList = onOpenSaved
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen({}, {}, {_ -> }, {}, {}, {}, {}, MainScreenViewModel())
}
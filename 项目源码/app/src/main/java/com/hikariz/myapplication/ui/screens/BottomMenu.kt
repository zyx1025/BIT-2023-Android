package com.hikariz.myapplication.ui.screens
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hikariz.myapplication.R

/*
底部导航栏
 */
sealed class BottomMenuScreen(
    val route: String,
    val icon: Int,
    val title: String
) {
    object ShowLocation : BottomMenuScreen("MapAndLocation",
        R.drawable.baseline_map_24, "显示当前位置")
    object SearchFound : BottomMenuScreen("SearchFound",
        R.drawable.baseline_search_24, "查询结果")
}

@Composable
fun BottomMenu(navController: NavController) {
    val menuItems = listOf(
        BottomMenuScreen.ShowLocation,
        BottomMenuScreen.SearchFound
    )
    BottomNavigation(contentColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            BottomNavigationItem(
                label = { Text(text = it.title) },
                alwaysShowLabel = true,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route)
                },
                icon = {
                    Icon(imageVector = ImageVector.vectorResource(id = it.icon), contentDescription = it.title)
                }
            )
        }
    }
}

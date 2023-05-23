package com.hikariz.myapplication.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amap.api.maps.AMap
import com.hikariz.myapplication.ui.screens.BottomMenu
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.MapView
import com.hikariz.myapplication.viewModel.DataBaseViewModel
import com.hikariz.myapplication.viewModel.DistanceViewModel

val LocalAMap = staticCompositionLocalOf<AMap?> { null }
@Composable
fun StartScreen() {
    //处理状态
    val scrollState = rememberScrollState()
    //获取NavHostController实例，以支持重组
    val navController = rememberNavController()
    //显示主页面，状态参数传给下一个组合函数
    MapAndLocationProvider {
        MainScreen(navController, scrollState)
    }
}

@Composable
fun MapAndLocationProvider(content: @Composable () -> Unit) {
    var aMap by remember { mutableStateOf<AMap?>(null) }
    CompositionLocalProvider(LocalAMap provides aMap) {
        content()
    }
}

@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(
        bottomBar = {
            BottomMenu(navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Navigation(navController, scrollState)
            }
        }
    )
}



@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState) {
    val distanceViewModel = viewModel<DistanceViewModel>()
    val databaseViewModel = viewModel<DataBaseViewModel>()
    NavHost(navController = navController, startDestination = "MapAndLocation") {
        composable("MapAndLocation") {
            MapAndLocation(distanceViewModel)
        }
        composable("SearchFound") {
            SearchFound(navController, distanceViewModel,databaseViewModel)
        }
    }
}
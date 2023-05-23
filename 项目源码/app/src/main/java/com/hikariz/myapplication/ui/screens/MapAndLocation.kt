package com.hikariz.myapplication.ui.screens

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.geocoder.GeocodeQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.TextureMapView
import com.hikariz.myapplication.ui.utils.MapViewContainer
import com.hikariz.myapplication.ui.utils.updateDistanceWithLatLng
import com.hikariz.myapplication.viewModel.DistanceViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapAndLocation(distanceViewModel: DistanceViewModel) {
    val context = LocalContext.current
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    if (locationPermissionState.allPermissionsGranted) {
        //当用户授予访问位置信息权限时，直接展示地图
        var mapView by remember { mutableStateOf<TextureMapView?>(null) }
        DisposableEffect(Unit) {
            onDispose {
                mapView?.onDestroy()
                mapView = null
            }
        }
        Column {
            MapViewContainer(
                modifier = Modifier.weight(1f),
                onMapReady = { mv ->
                    mapView = mv
                },
                onMapViewDispose = {
                    mapView = null
                }
            )
            mapView?.map?.let { aMap ->
                displayLocation(aMap, context, distanceViewModel)
            }
        }
    } else {
        //当用户没有授予访问位置信息权限时，需要向用户申请（这也是程序的进入画面）
        Column(
            modifier = Modifier
                .fillMaxSize() // Make sure the Column occupies all available space
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween, // Space elements evenly
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "使用说明",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center, // Center align the text
                fontSize = 32.sp // Increase the font size
            )
            Text(
                text = "本app可以查询当前时间段文萃楼的空教室，获取位置权限并在显示当前位置的小蓝点出现后，查询结果页面会按照你的位置,由近到远显示空教室。",
                lineHeight = 20.sp
            )
            Text(
                text = "注意事项",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center, // Center align the text
                fontSize = 32.sp // Increase the font size
            )
            Text(
                text = "1.如果没出现蓝点，可以点击右上角的定位按钮。若仍然未显示，请检查手机的位置信息访问权限是否打开" +'\n'+ "2.如果未授予权限，查询结果页面只会显示空教室，而不会按照距离自动排序",
                lineHeight = 20.sp,
                color = Color.Red
            )
            // Use weight to push the button to the bottom
            Spacer(Modifier.weight(1f))
            Button(onClick = { locationPermissionState.launchMultiplePermissionRequest() }) {
                Text("请求权限")
            }
        }
    }
}

//在用户授予权限并生成地图后，添加用户位置的蓝点，并自动更新当前位置到六栋楼的距离
@Composable
private fun displayLocation(aMap: AMap, context: Context,distanceViewModel: DistanceViewModel) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    // 设置蓝点表示用户当前位置
    LaunchedEffect(aMap) {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.strokeColor(0)
        myLocationStyle.radiusFillColor(0)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        aMap.myLocationStyle = myLocationStyle
        aMap.uiSettings.isMyLocationButtonEnabled = true
        aMap.isMyLocationEnabled = true
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19f))
    }

    // 高德地图 SDK 的隐私政策同意与展示
    LaunchedEffect(Unit) {
        AMapLocationClient.updatePrivacyAgree(context, true)
        AMapLocationClient.updatePrivacyShow(context, true, true)
    }

    val locationClient = remember {
        AMapLocationClient(context).apply {
            setLocationOption(AMapLocationClientOption().apply {
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                interval = 1000
            })

            setLocationListener { location ->
                if (location != null && location.errorCode == 0) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    currentLocation = latLng
                }
            }
            startLocation()
        }
    }

    /*六栋楼的经纬度（大致）
           B:116.174499,39.732275
           F:116.173839,39.732106
           G:116.173244,39.732291
           H:116.17312,39.733149
           I:116.173828,39.733199
           M:116.174515,39.733191
    */
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            val destinationLatLngB = LatLng(39.732275,116.174499)
            updateDistanceWithLatLng(currentLocation, destinationLatLngB) { distance ->
                distanceViewModel.updateDistanceB(distance)
            }
            val destinationLatLngF = LatLng(39.732106,116.173839)
            updateDistanceWithLatLng(currentLocation, destinationLatLngF) { distance ->
                distanceViewModel.updateDistanceF(distance)
            }
            val destinationLatLngG = LatLng(39.732291,116.173244)
            updateDistanceWithLatLng(currentLocation, destinationLatLngG) { distance ->
                distanceViewModel.updateDistanceG(distance)
            }
            val destinationLatLngH = LatLng(39.733149,116.17312)
            updateDistanceWithLatLng(currentLocation, destinationLatLngH) { distance ->
                distanceViewModel.updateDistanceH(distance)
            }
            val destinationLatLngI = LatLng(39.733199,116.173828)
            updateDistanceWithLatLng(currentLocation, destinationLatLngI) { distance ->
                distanceViewModel.updateDistanceI(distance)
            }
            val destinationLatLngM = LatLng(39.733191,116.174515)
            updateDistanceWithLatLng(currentLocation, destinationLatLngM) { distance ->
                distanceViewModel.updateDistanceM(distance)
            }
        }
    }
    DisposableEffect(locationClient) {
        onDispose {
            locationClient.onDestroy()
        }
    }
}




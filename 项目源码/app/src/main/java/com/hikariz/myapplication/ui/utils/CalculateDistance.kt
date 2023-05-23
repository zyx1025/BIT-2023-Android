package com.hikariz.myapplication.ui.utils

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng

fun updateDistanceWithLatLng(
    currentLocation: LatLng?,
    destinationLatLng: LatLng,
    onDistanceResult: (Double) -> Unit
) {
    if (currentLocation == null){
        return
    }
    val distance = AMapUtils.calculateLineDistance(currentLocation, destinationLatLng)
    onDistanceResult(distance.toDouble())
}

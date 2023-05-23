package com.hikariz.myapplication.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleObserver
import com.amap.api.maps.TextureMapView

//创建MapView用
@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    onMapReady: (TextureMapView) -> Unit,
    onMapViewDispose: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapView = remember {
        TextureMapView(context).apply {
            onCreate(null)
            onMapReady(this)
        }
    }

    DisposableEffect(key1 = lifecycle, key2 = mapView) {
        val lifecycleObserver = object : LifecycleObserver {
            fun onResume() {
                mapView.onResume()
            }

            fun onPause() {
                mapView.onPause()
            }

            fun onDestroy() {
                mapView.onDestroy()
                onMapViewDispose?.invoke()
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
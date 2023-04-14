package com.cooptest.mykotlin.barcode

import android.Manifest
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import com.cooptest.mykotlin.R
import com.cooptest.mykotlin.ui.theme.MyKotlinTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(
    ExperimentalPermissionsApi::class,
)
@Composable
@ExperimentalGetImage
fun BarcodeScannerScreen(
    viewModel: BarcodeScanViewModel,
    onScreenLoaded: (Boolean) -> Unit = {},
    onPermissionGrantedResult: (Boolean) -> Unit = {},
) {

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA,
        onPermissionResult = {
            onPermissionGrantedResult.invoke(it)
        }
    )

    val progressscan = viewModel.progressScan.observeAsState(false)

    LaunchedEffect(key1 = LocalLifecycleOwner.current) {
        val status = cameraPermissionState.status
        if (!status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
        onScreenLoaded.invoke(status.isGranted)
    }






    MyKotlinTheme {

        Scaffold(
            topBar = {
 },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                when (cameraPermissionState.status) {
                    is PermissionStatus.Granted -> {
                        BarcodeScannerView(
                            imageAnalyzer = viewModel.getBarcodeImageAnalyzer(),viewModel,progressscan = progressscan.value
                        )
                    }
                    is PermissionStatus.Denied -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = stringResource(id = R.string.camera_Permission_denied),
                            )
                        }
                    }
                }

            }

        }

    }

}



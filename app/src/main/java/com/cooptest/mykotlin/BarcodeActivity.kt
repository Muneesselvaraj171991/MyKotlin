package com.cooptest.mykotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cooptest.mykotlin.barcode.BarcodeScannerScreen
import com.cooptest.mykotlin.barcode.BarcodeScanViewModel

@ExperimentalGetImage
class BarcodeActivity : ComponentActivity() {

    private val barcodeScanner = BarcodeOptions.provideBarcodeScanner()
    private val viewModel by viewModels<BarcodeScanViewModel>(
        factoryProducer = {
            viewModelFactory {
                initializer {
                    BarcodeScanViewModel(
                        barcodeScanner = barcodeScanner,
                        barcodeImageAnalyzer = BarcodeOptions.provideBarcodeImageAnalyzer(
                            barcodeScanner
                        ),
                    )
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            window?.setStatusBarColor(Color.Black.toArgb())


            BarcodeScannerScreen(viewModel = viewModel)
        }
    }




}
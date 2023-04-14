package com.cooptest.mykotlin.barcode

import androidx.camera.core.ExperimentalGetImage
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cooptest.mykotlin.BarcodeOptions
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode

@ExperimentalGetImage
class BarcodeScanViewModel constructor(
    private val barcodeScanner: BarcodeScanner,
    private val barcodeImageAnalyzer: ImageAnalyzer = BarcodeOptions.provideBarcodeImageAnalyzer(barcodeScanner),
    ) : ViewModel() {

    init {
        setupImageAnalyzer()
    }
    var stringValue = MutableLiveData<String>("Handla Online")
    var scancount = MutableLiveData<Int>(0)
    var progressScan = MutableLiveData<Boolean>(false)
    var barCodeValue = MutableLiveData<String>()

    val progresss: Boolean? = progressScan.value



    fun updateString(string: String) {
        stringValue.postValue(string)
    }
    fun getBarcodeImageAnalyzer(): ImageAnalyzer {
        return barcodeImageAnalyzer
    }
    private fun setupImageAnalyzer() {
        barcodeImageAnalyzer.setProcessListener(
            listener = object : ImageAnalyzer.ProcessListenerAdapter() {
                override fun onSucceed(results: List<Barcode>) {
                    handleBarcodeResults(results)
                }
            }
        )
    }

    private fun handleBarcodeResults(results: List<Barcode>) {
        when {
            progresss == false -> {
                results.forEach { barcode ->
                    barcode.rawValue?.let { barcodeValue ->
                        barCodeValue.postValue(barcodeValue)

                    }
                }
                progressScan(true)
            }

        }

    }

    fun progressScan(progressscan: Boolean) {
        progressScan.postValue(progressscan)
    }
}

package com.cooptest.mykotlin.barcode


import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.cooptest.mykotlin.*
import com.cooptest.mykotlin.R
import com.cooptest.mykotlin.ui.theme.black
import com.cooptest.mykotlin.ui.theme.colorPrimary
import com.cooptest.mykotlin.ui.theme.everGreen
import com.cooptest.mykotlin.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalGetImage
@Composable
fun BarcodeScannerView(

    imageAnalyzer: ImageAnalyzer,
    viewModel : BarcodeScanViewModel, progressscan: Boolean

) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    var camera by remember { mutableStateOf<Camera?>(null) }


    val stringName: String? = viewModel.stringValue.observeAsState().value
    val barcodeValue: String? = viewModel.barCodeValue.observeAsState().value

    val cameraPreview = Preview.Builder().build()

    val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val previewWidget = remember { PreviewView(context) }

    val animatedProgress = remember { Animatable(0.001f) }
    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(1f,
            animationSpec = repeatable(2,
                animation = tween(durationMillis = 3000, easing = LinearEasing)
            ))
    }

    suspend fun setupCameraPreview() {
         val cameraProvider = context.getCameraXProvider()
        cameraProvider.unbindAll()
       camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreview,
            imageAnalysis,
        )
        cameraPreview.setSurfaceProvider(previewWidget.surfaceProvider)
        imageAnalysis.setAnalyzer(imageAnalyzer.getAnalyzerExecutor(), imageAnalyzer)
    }

    LaunchedEffect(key1 = configuration) {
        setupCameraPreview()
    }

    val scaffoldState: ScaffoldState = rememberScaffoldState()


    Scaffold(scaffoldState = scaffoldState) {

        LaunchedEffect(key1 = progressscan) {
            if (progressscan) {
                viewModel.scanCountIncrement()
                val job = launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Hi",
                        duration = SnackbarDuration.Indefinite
                    )
                }
                delay(500)
                job.cancel()
            }
            try {
                initTimer(2000) {
                    viewModel.progressScan(false)
                }
            } catch (e: Exception) {
            }

        }
    }



    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { previewWidget },
            modifier = Modifier
                .fillMaxSize()

        )

                    Box(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.fontsize_bar))
                    .align(Alignment.Center)

            ) {

                Canvas(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.fontsize_bar))
//                        .scale(scale)
                        .align(Alignment.Center)
                        .padding(dimensionResource(id = R.dimen.fontsize_16dp)),

                    ) {



                    drawQrBorderCanvas(
                        curve = 40.dp,
                        strokeWidth = 5.dp,
                        capSize = 40.dp,
                        gapAngle = 30
                    )

                }
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.test4),
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.fontsize_8dp)),
                            contentDescription = ""
                        )
                        Image(
                            painter = painterResource(id = R.drawable.test4),
                            contentDescription = ""
                        )
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = dimensionResource(id = R.dimen.fontsize_10dp)),
                        text = stringResource(id = R.string.rikta_text),
                        style = MaterialTheme.typography.subtitle2.copy(color = white)

                    )

                }

            }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            camera?.let {
                    BottomBar(it,viewModel,stringName.toString())

            }
        }
        stringName?.let { CustomScanTopBar(context,it) }
        BackHandler() {
            onBackPressed(context)
        }
    }
}

fun onBackPressed(context :Context){
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}


@Composable
fun CustomScanTopBar(context: Context,stringName:String) {
    TopAppBar(
        elevation = dimensionResource(id = R.dimen.elevation),
        modifier = Modifier
            .fillMaxWidth()

            .background(black.copy(alpha = 0.6f)),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringName,
                    modifier = Modifier.align(Alignment.Center),
                    color = white,
                    fontSize = dimensionResource(id = R.dimen.subtext_fontsize).value.sp

                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        onBackPressed(context)
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = stringResource(id = R.string.canel)
                    )
                }
            }
        },
    )
}

@Composable
fun BottomBar(camera:Camera, viewModel: BarcodeScanViewModel, stringName: String) {
    var flashEnabled by remember { mutableStateOf(false) }
    val scanCount by viewModel.scancount.observeAsState(0)

    Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_))
                    .fillMaxWidth()

            ) {


                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        if (flashEnabled) {
                            flashEnabled = false
                            camera.cameraControl.enableTorch(flashEnabled)
                        } else {
                            flashEnabled = true
                            camera.cameraControl.enableTorch(flashEnabled)

                        }
                    }
                ) {

                    Image(
                        painter = painterResource(
                            id = if (flashEnabled) {
                                R.drawable.flashlight_off
                            } else {
                                R.drawable.flashlight_off
                            }
                        ),
                        contentDescription = stringResource(id = R.string.flash_light)
                    )
                }
                BadgedBox(
                    badge = {
                        colorPrimary
                        if(scanCount!=0) {
                            Badge(
                                backgroundColor= colorPrimary
                            ) {
                                Text(text = "${scanCount}",color= white)
                            }
                        }
            }
                ){

                    if(stringName.equals(stringResource(id = R.string.handla_online))){
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = white),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.fontsize_16dp)),
                        onClick = {
                        }

                    ) {

                        Text(
                            text = stringResource(id = R.string.till_varuga),
                            style = MaterialTheme.typography.subtitle2.copy(color = everGreen)
                        )
                        Spacer(modifier = Modifier.width(width = dimensionResource(id = R.dimen.fontsize_8dp)))
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = stringResource(id = R.string.arrowIcon)
                        )

                    }}
                }

            }
            MotionLayoutButton(viewModel)
}


suspend fun initTimer(time: Long, onEnd: () -> Unit) {
    delay(timeMillis = time)
    onEnd()
}
private fun DrawScope.drawQrBorderCanvas(
    borderColor: Color = Color.White,
    curve: Dp,
    strokeWidth: Dp,
    capSize: Dp,
    gapAngle: Int = 20,
    shadowSize: Dp = strokeWidth * 2,
    cap: StrokeCap = StrokeCap.Square,
    lineCap: StrokeCap = StrokeCap.Round,
) {

    val curvePx = curve.toPx()

    val mCapSize = capSize.toPx()

    val width = size.width
    val height = size.height

    val sweepAngle = 90 / 1- gapAngle / 2f

    strokeWidth.toPx().toInt()
    for (i in 4..shadowSize.toPx().toInt() step 2) {
        drawRoundRect(
            color = Color(0x0000000),
            size = size,
            topLeft = Offset(0f, 0f),
            style = Stroke(width = i * 1f),
            cornerRadius = CornerRadius(
                x = curvePx,
                y = curvePx
            ),
        )
    }

    val mCurve = curvePx * 2

    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 0f,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            width - mCurve , height - mCurve
        )
    )
    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 90 - sweepAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            width - mCurve , height - mCurve
        )
    )

    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve, mCurve),
        topLeft = Offset(
            0f, height - mCurve
        )
    )
    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 180 - sweepAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            0f, height - mCurve
        )
    )

    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 180f,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            0f, 0f
        )
    )

    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 270 - sweepAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            0f, 0f
        )
    )


    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 270f,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve, mCurve ),
        topLeft = Offset(
            width - mCurve , 0f
        )
    )

    drawArc(
        color = borderColor,
        style = Stroke(strokeWidth.toPx(), cap = cap),
        startAngle = 360 - sweepAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        size = Size(mCurve , mCurve ),
        topLeft = Offset(
            width - mCurve , 0f
        )
    )


    drawLine(
        SolidColor(borderColor), Offset(width, height - mCapSize), Offset(width, height - curvePx),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(width - mCapSize, height), Offset(width - curvePx, height),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(mCapSize, height), Offset(curvePx, height),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(0f, height - curvePx), Offset(0f, height - mCapSize),
        strokeWidth.toPx(), lineCap
    )

    drawLine(
        SolidColor(borderColor), Offset(0f, curvePx), Offset(0f, mCapSize),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(curvePx, 0f), Offset(mCapSize, 0f),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(width - curvePx, 0f), Offset(width - mCapSize, 0f),
        strokeWidth.toPx(), lineCap,
    )

    drawLine(
        SolidColor(borderColor), Offset(width, curvePx), Offset(width, mCapSize),
        strokeWidth.toPx(), lineCap
    )

}


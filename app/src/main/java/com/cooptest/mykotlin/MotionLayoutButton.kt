package com.cooptest.mykotlin


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import com.cooptest.mykotlin.barcode.BarcodeScanViewModel
import com.cooptest.mykotlin.ui.theme.black
import com.cooptest.mykotlin.ui.theme.colorPrimary
import com.cooptest.mykotlin.ui.theme.white
import java.util.*
import kotlin.math.roundToInt




@OptIn( ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
fun MotionLayoutButton(viewModel: BarcodeScanViewModel) {
    Column(){
        val stringName: String? = viewModel.stringValue.observeAsState().value


        ///var componentHeight by remember { mutableStateOf(1000) }

        val swipeableState = rememberSwipeableState(1)
        val anchors = mapOf(0f to 0, 1f to 1)
// on below line we are specifying animate button method.
        var animateButton by remember { mutableStateOf(false) }
// on below line we are specifying button animation progress
        val buttonAnimationProgress by animateFloatAsState(

// specifying target value on below line.
            targetValue = if (animateButton) 1f else 0f,

// on below line we are specifying
// animation specific duration's 1 sec
            animationSpec = tween(1000)
        )


// on below line we are creating a motion layout.
        MotionLayout(

// in motion layout we are specifying 2 constraint
// set for two different positions of buttons.
// in first constraint set we are specifying width,
// height start, end and top position of buttons.
            ConstraintSet(
                """ {

guide : {
type: 'vGuideline',
percent: 0.40
},
guide1 : {
type: 'vGuideline',
percent: 0.4
},
button1: {
width: 'wrap',
height: 'wrap',
start: ['guide1', 'start'],
//end: ['button2', 'start', 10]
},
// on below line we are specifying width,height
// and margin from start, top and end for button2
button2: {
width: 'wrap',
height: 'wrap',
start: ['button1', 'end'],
//end: ['guide', 'end', 30]
}
} """
            ),

// in second constraint set we are specifying width,
// height start, end and top position of buttons.
            ConstraintSet(
                """ {
// on below line we are specifying width,height and margin
// from start, top and end for button1

guide : {
type: 'vGuideline',
percent: 0.40
},
guide1 : {
type: 'vGuideline',
percent: 0.55
},
button1: {
width: 'wrap',
height: 'wrap',
//start: ['parent', 'start', 30],
end: ['button2', 'start']
},
// on below line we are specifying width,height
// and margin from start, top and end for button2
button2: {
width: 'wrap',
height: 'wrap',
end: ['guide1', 'end'],
//end: ['guide1', 'end', 30]
}
} """
            ),
            progress = buttonAnimationProgress,
            debug = EnumSet.of(MotionLayoutDebugFlags.NONE),
            modifier = Modifier
                .fillMaxWidth()
                .size(dimensionResource(id = R.dimen.motionlayout_size))
                .background(black.copy(alpha = 0.6f))
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    reverseDirection = true,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .onSizeChanged { size ->
                    // componentHeight = size.height.toFloat()
                }
        ) {


            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.padding_))
                    .layoutId("button1"),
                horizontalArrangement = Arrangement . Center

            ) {
                val string : String = stringResource(id = R.string.handla_online)
                Image(
                    painter = painterResource(id = R.drawable.bucket_selected),
                    contentDescription = "",
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.bucket_size))
                        .width(dimensionResource(id = R.dimen.bucket_size))
                        .clickable {
                            viewModel.updateString(string)
                            animateButton = !animateButton
                        }


                )
                val string1 : String = stringResource(id = R.string.coops_hållbarhetsdeklaration)

                Image(
                    painter = painterResource(id = R.drawable.search_selected),
                    contentDescription = "",
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.bucket_size))
                        .width(dimensionResource(id = R.dimen.bucket_size))
                        .clickable {
                            viewModel.updateString(string1)
                            animateButton = !animateButton

                        }
                )

            }



        }


       Row (modifier = Modifier
           .background(black.copy(alpha = 0.6f))
           .fillMaxWidth()
           .padding(bottom = dimensionResource(id = R.dimen.padding_))){
           stringName?.let {
               Text(
                   textAlign = TextAlign.Center,
                   text = it,
                   modifier = Modifier

                       .fillMaxWidth(),
                   style = MaterialTheme.typography.subtitle2.copy(color = white)
               )
           }
       }

    }

}
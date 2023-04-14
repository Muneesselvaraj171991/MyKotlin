package com.cooptest.mykotlin
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutDebugFlags
import com.cooptest.mykotlin.barcode.BarcodeScanViewModel
import com.cooptest.mykotlin.ui.theme.black
import com.cooptest.mykotlin.ui.theme.white
import java.util.EnumSet


@OptIn( ExperimentalMotionApi::class)
@Composable
fun  MotionLayoutButton(viewModel: BarcodeScanViewModel) {
    Column {

        var isScanButtonPressed  by remember { mutableStateOf(true)}

        val stringName: String? = viewModel.stringValue.observeAsState().value

        var animateButton by remember { mutableStateOf(false) }

        val buttonAnimationProgress by animateFloatAsState(

            targetValue = if (animateButton) 1f else 0f,

            animationSpec = tween(1000)
        )


        MotionLayout(

            ConstraintSet(
                """ {


guide1 : {
type: 'vGuideline',
percent: 0.4
},
button1: {
width: 'wrap',
height: 'wrap',
start: ['guide1', 'end'],
},
button2: {
width: 'wrap',
height: 'wrap',
start: ['button1', 'end'],
}
} """
            ),

// in second constraint set we are specifying width,
// height start, end and top position of buttons.
            ConstraintSet(
                """ {
// on below line we are specifying width,height and margin
// from start, top and end for button1


guide1 : {
type: 'vGuideline',
percent: 0.64
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
start: ['guide1', 'start'],
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


        ) {


            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.padding_))
                    .layoutId("button1"),
                horizontalArrangement = Arrangement . Center

            ) {
                val scan : String = stringResource(id = R.string.handla_online)
                Image(
                    painter = painterResource(id = if (isScanButtonPressed) {
                        if(viewModel.scancount.value != 0)  R.drawable.bucket_selected else R.drawable.sample;
                    } else {
                        R.drawable.bucket_item_un_selected
                    }),
                    contentDescription = "",

                    modifier = Modifier

                        .clickable {

                            if(!isScanButtonPressed) {
                                isScanButtonPressed = true
                                viewModel.updateString(scan)
                                animateButton = !animateButton
                            }
                        }


                )
                val search : String = stringResource(id = R.string.coops_hållbarhetsdeklaration)

                Image(
                    painter = painterResource(id = if (!isScanButtonPressed) {
                        R.drawable.search_selected
                    } else {
                        R.drawable.search_unselected
                    }),
                    contentDescription = "",
                    modifier = Modifier

                        .clickable {
                            if(isScanButtonPressed) {
                                isScanButtonPressed = false

                                viewModel.updateString(search)
                                animateButton = !animateButton
                            }

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
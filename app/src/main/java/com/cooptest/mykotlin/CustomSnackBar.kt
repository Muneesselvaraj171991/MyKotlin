package com.cooptest.mykotlin

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cooptest.mykotlin.ui.theme.colorPrimary
import com.cooptest.mykotlin.ui.theme.transparent
import kotlinx.coroutines.launch

@Composable
fun CustomSnackBar() {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) { contentPadding ->
        ScaffoldSnackbar(scaffoldState,contentPadding)
    }
//    Column {
//        val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
//
//        Button(onClick = { setSnackBarState(!snackbarVisibleState) }) {
//            if (snackbarVisibleState) {
//                Text("Hide Snackbar")
//            } else {
//                Text("Show Snackbar")
//            }
//        }
//        if (snackbarVisibleState) {
//            Snackbar(
//
//                action = {
//                    Button(onClick = {}) {
//                        Text("MyAction")
//                    }
//                },
//                modifier = Modifier.padding(8.dp)
//            ) { Text(text = "This is a snackbar!") }
//        }
//    }


//    val context = LocalContext.current
//    var snackbarState = remember {
//        SnackbarHostState()
//    }
//var coroutineScope = rememberCoroutineScope()
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize(),
//
//                color = MaterialTheme.colors.primarySurface,
//        elevation = 8.dp
//    ) {
//        Button(onClick = {}) {
//
//        }
//        SnackbarHost(hostState = snackbarState){
//        customSnackbar(title =  "Munees", content = "munees", profileImageResource = R.drawable.search, onAction = {
//
//        })
//        }
//    }
 
    
}
@Composable
fun ScaffoldSnackbar(state: ScaffoldState, contentPadding: PaddingValues) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column() {
            coroutineScope.launch {
                val result = state.snackbarHostState.showSnackbar(
                    message = "Button clicked",
                    duration = SnackbarDuration.Short,
                    actionLabel = "Action"
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        Toast.makeText(context, "Snackbar action performed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    SnackbarResult.Dismissed -> {
                        Toast.makeText(context, "Snackbar action performed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

    }
}
@Composable
fun customSnackbar(
    modifier: Modifier = Modifier,
    title:String ,
    content :String,
    profileImageResource : Int,
    onAction: () -> Unit

){
    Snackbar(elevation = 0.dp,
    backgroundColor = transparent) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight())
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))) {
            Text(text = "Munees")
        }
    }
}
@Composable
fun CustomSnackBar1(
    @DrawableRes drawableRes: Int,
    message: String,
    isRtl: Boolean = true,
    containerColor: Color = Color.Black
) {
//    var count by remember { mutableStateOf(0) }
//
//    Snackbar() {
//        CompositionLocalProvider(
//            LocalLayoutDirection provides
//                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
//        ) {
//            Row {
//
//                Icon(
//                    painterResource(id = drawableRes),
//                    contentDescription = null
//                )
//                Text(message)
//            }
//        }
//    }

//    TransientSnackbar(
//        snackbarState = SnackbarState(),
//        text = "test",
//        actionLabel ="eeee" ,
//        onAction = { /*TODO*/ },
//    )

}
class SnackbarState {
   public var show by mutableStateOf(false)
}


@Composable
fun TransientSnackbar(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarState,
    text: String,
    actionLabel: String,
    onAction: () -> Unit,
    shape: Shape = MaterialTheme.shapes.small,
    autoDismiss: Boolean = true,
    timeout: Long = 4_000,
    onDismiss: (SnackbarState) -> Unit
) {

    if(snackbarState.show) {
        Snackbar(
            modifier = modifier,

            shape = shape,
            action = {
                ClickableText(
                    text = AnnotatedString(text = actionLabel),
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    ),
                    onClick = {
                        onAction()
                        snackbarState.show = false
                    }
                )
            }
        ){

        }
    }

    if (autoDismiss && snackbarState.show) {

            snackbarState.show = false
            onDismiss(snackbarState)

    }
}
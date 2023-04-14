package com.cooptest.mykotlin

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.cooptest.mykotlin.ui.theme.black
import com.cooptest.mykotlin.ui.theme.ghost_white


@Composable
fun DashboardScreen() {
        Scaffold(
            Modifier.background(ghost_white),
            topBar = {
                CustomTopAppBar()
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = paddingValues.calculateTopPadding()
                    )
                    .background(
                        ghost_white
                    )

            ) {
              // HomeScreen()
                CustomSnackBar()
            }
        }
        }


@Composable
fun CustomTopAppBar() {
    TopAppBar(
        elevation = dimensionResource(id = R.dimen.elevation),
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id =R.string.handla),
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = black,
                    fontSize = dimensionResource(id = R.dimen.handla_fontsize).value.sp)
            }
        },
        backgroundColor = ghost_white,
        actions = {
            AppBarActions()
        }
    )
}
@Composable
fun AppBarActions() {
    SearchAction()
    BarcodeButton()
}

@Composable
fun SearchAction() {
    IconButton(
        onClick = {
        }) {
        Image(
            painter = painterResource(id = R.drawable.search),
            contentDescription = stringResource(id = R.string.search_icon),
        )
    }
}
@Composable
fun BarcodeButton() {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = Intent(context, BarcodeActivity::class.java)
            context.startActivity(intent)
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.barcode ),
            contentDescription = stringResource(id = R.string.barcode_icon),
        )
    }
}



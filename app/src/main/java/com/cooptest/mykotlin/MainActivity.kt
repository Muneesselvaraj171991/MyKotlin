package com.cooptest.mykotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import com.cooptest.mykotlin.ui.theme.MyKotlinTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window?.statusBarColor = colorResource(id = R.color.ghost_white).toArgb()


            MyKotlinTheme {

                DashboardScreen()
                BackHandler {
                    finishAffinity()
                    // your action
                }
            }

        }

    }
}
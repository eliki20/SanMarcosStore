package com.example.sanmarcosstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.sanmarcosstore.ui.navigation.AppNavigation
import com.example.sanmarcosstore.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var darkTheme by remember {
                mutableStateOf(false)
            }

            AppTheme(
                darkTheme = darkTheme,
                dynamicColor = false
            ) {

                AppNavigation(
                    darkTheme = darkTheme,
                    onThemeChange = {
                        darkTheme = it
                    }
                )
            }
        }
    }
}
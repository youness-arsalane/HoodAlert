package com.example.hoodalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hoodalert.theme.HoodAlertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HoodAlertTheme {
                HoodAlertNavHost(context = this)
            }
        }
    }
}
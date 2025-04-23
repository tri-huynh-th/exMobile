package com.example.bt5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.bt5.ui.theme.Bt5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bt5Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

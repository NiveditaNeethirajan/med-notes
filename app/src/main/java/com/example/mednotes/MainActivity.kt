package com.example.mednotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mednotes.ui.navigation.destination.Destination
import com.example.mednotes.ui.navigation.navhost.MainNavHost
import com.google.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ProvideWindowInsets {
                MainNavHost(
                    navController = navController,
                    startDestination = Destination.ScheduleScreen
                )
            }
        }
    }
}
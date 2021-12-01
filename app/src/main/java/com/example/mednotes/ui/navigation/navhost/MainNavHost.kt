package com.example.mednotes.ui.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mednotes.ui.navigation.destination.Destination
import com.example.mednotes.ui.schedule.ScheduleViewComponent

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: Destination = Destination.ScheduleScreen,
) {
    NavHost(navController, startDestination = startDestination.route) {

        composable(Destination.ScheduleScreen.route) {
            ScheduleViewComponent ()
        }
    }
}
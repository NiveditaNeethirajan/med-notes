package com.example.mednotes.ui.navigation.destination

sealed class Destination(val route: String) {

    object ScheduleScreen : Destination("ScheduleScreen")
}
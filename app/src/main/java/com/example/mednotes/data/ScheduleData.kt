package com.example.mednotes.data

data class ScheduleData(
    val label: String,
    val moments : List<ScheduleMomentData>,
    var isCollapsed: Boolean
)

data class ScheduleMomentData(
    val title: String,
    val timeLabel: String,
    val iconResource: Int,
    val withMedicines: List<Medicine>,
    var isCollapsed: Boolean = false
)
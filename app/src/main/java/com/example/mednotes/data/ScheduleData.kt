package com.example.mednotes.data

import java.util.*

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

data class ScheduleWeeklyData(
    val timeLabel: String,
    val data: List<ScheduleWeeklyMedicineData>,
)

data class ScheduleWeeklyMedicineData(
    val medicineName: String,
    var weeklyMomentData: List<ScheduleWeeklyMomentData>
)

data class ScheduleWeeklyMomentData(
    val momentTitle: String,
    val medicineName: String,
    val isTaken: Boolean,
    val day: String,
    val time: String
)

enum class ScheduleViewMode {
    Week,
    Month
}
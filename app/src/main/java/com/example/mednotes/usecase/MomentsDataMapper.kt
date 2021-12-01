package com.example.mednotes.usecase

import com.example.mednotes.data.Moment
import com.example.mednotes.data.ScheduleData
import com.example.mednotes.data.ScheduleMomentData
import java.text.SimpleDateFormat
import java.util.*

fun List<Moment>.toMonthlyScheduleMomentsData() : List<ScheduleData> {
    if (this.isEmpty())
        return emptyList()
    val scheduleDataList = mutableListOf<ScheduleData>()
    val sortedData = this.sortedBy { it.at }
    val firstDate = sortedData.first().at
    (0..30).map { day ->
        val start = firstDate.getStartOfDay(day)
        val moments = sortedData.filter {
            it.at >= start && it.at <= firstDate.getEndOfDay(day)
        }
        if (moments.isNotEmpty()) {
            scheduleDataList.add(
                ScheduleData(
                    label = formatLabel(start),
                    moments = moments.toScheduleMomentData(),
                    isCollapsed = false
                )
            )
        }
    }
    return scheduleDataList
}

private fun List<Moment>.toScheduleMomentData(): List<ScheduleMomentData> {
    return this.map { moment ->
        ScheduleMomentData(
            title =  moment.title,
            timeLabel = formatTime(moment.at),
            iconResource = moment.withIcon,
            withMedicines = moment.withMedicines,
            isCollapsed = moment.isCollapsed
        )
    }
}

private fun formatLabel(date: Date): String {
    val pattern = "EEEE dd MMMM"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}

private fun formatTime(date: Date): String {
    val pattern = "HH:mm"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}
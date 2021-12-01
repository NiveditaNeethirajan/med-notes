package com.example.mednotes.usecase

import com.example.mednotes.data.*
import java.text.SimpleDateFormat
import java.util.*


private const val NUMBER_OF_WEEKS = 52

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

fun List<Moment>.toWeeklyScheduleMomentsData(): List<ScheduleWeeklyData> {
    if (this.isEmpty())
        return emptyList()
    val scheduleDataList = mutableListOf<ScheduleWeeklyData>()
    val sortedData = this.sortedBy { it.at }
    val startDate = sortedData.last().at
    (-NUMBER_OF_WEEKS..0).map { week ->
        val start = startDate.getStartOfWeek(week)
        val end = start.getEndOfWeek()
        val moments = sortedData.filter {
            it.at in start..end
        }
        if (moments.isNotEmpty()) {
            scheduleDataList.add(
                ScheduleWeeklyData(
                    timeLabel  = formatLabel(start, ScheduleViewMode.Week),
                    medicineName = moments.toMedicineNames()
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

private fun List<Moment>.toMedicineNames(): List<String> {
    return this.map { moment ->
        moment.withMedicines.map { it.name }.distinct().joinToString()
    }
}

private fun formatLabel(date: Date, mode: ScheduleViewMode): String {
    val pattern = when (mode) {
        ScheduleViewMode.Month -> "EEEE dd MMMM"
        ScheduleViewMode.Week -> "w"
    }
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val title = simpleDateFormat.format(date)
    return if(mode == ScheduleViewMode.Week) {
         "Week no - "+title + ") " +getStartEndOfWeek(date)
    } else title
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

private fun getStartEndOfWeek(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.firstDayOfWeek = Calendar.MONDAY;
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val mondayOffset = if (dayOfWeek == 1) {
        -6
    } else {
        2 - dayOfWeek
    }
    calendar.add(Calendar.DATE, mondayOffset)
    val endOfWeek = calendar.time.getEndOfWeek()
    val endCalendar = Calendar.getInstance()
    endCalendar.time = endOfWeek
    return String.format(
        "%02d - %02d ",
        calendar.get(Calendar.DATE),
        endCalendar.get(Calendar.DATE)
    )
}
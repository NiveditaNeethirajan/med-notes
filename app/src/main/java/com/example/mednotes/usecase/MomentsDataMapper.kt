package com.example.mednotes.usecase

import com.example.mednotes.data.*
import java.text.SimpleDateFormat
import java.util.*


private const val NUMBER_OF_WEEKS = 52

fun List<Moment>.toMonthlyScheduleMomentsData(): List<ScheduleData> {
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
                    label = formatLabel(start, ScheduleViewMode.Month),
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
            val data = moments.toScheduleWeeklyMedicineData()
            val filteredData = mutableListOf<ScheduleWeeklyMedicineData>()
            data.forEach { medicineData ->
                val duplicate =
                    filteredData.firstOrNull { it.medicineName == medicineData.medicineName }
                if (duplicate == null) {
                    filteredData.add(medicineData)
                } else {
                    filteredData.remove(duplicate)
                    filteredData.add(
                        ScheduleWeeklyMedicineData(
                            medicineName = medicineData.medicineName,
                            weeklyMomentData =
                            duplicate.weeklyMomentData.plus(medicineData.weeklyMomentData)
                        )
                    )
                }
            }
            scheduleDataList.add(
                ScheduleWeeklyData(
                    timeLabel = getStartEndOfWeek(start),
                    data = filteredData
                )
            )
        }
    }
    return scheduleDataList
}

private fun List<Moment>.toScheduleWeeklyMedicineData(): List<ScheduleWeeklyMedicineData> {
    val scheduleWeeklyMedicineDataList = mutableListOf<ScheduleWeeklyMedicineData>()
    this.map { moment ->
        moment.withMedicines.forEach { medicine ->
            scheduleWeeklyMedicineDataList.add(
                ScheduleWeeklyMedicineData(
                    weeklyMomentData = toScheduleWeeklyMomentData(moment),
                    medicineName = medicine.name,
                )
            )
        }
    }
    return scheduleWeeklyMedicineDataList
}

private fun toScheduleWeeklyMomentData(moment: Moment): List<ScheduleWeeklyMomentData> {
    val scheduleWeeklyMomentDataList = mutableListOf<ScheduleWeeklyMomentData>()
    moment.withMedicines.forEach { medicine ->
        scheduleWeeklyMomentDataList.add(
            ScheduleWeeklyMomentData(
                momentTitle = moment.title,
                medicineName = medicine.name,
                isTaken = medicine.isTaken,
                day = formatDay(moment.at),
                time = formatTime(moment.at)
            )
        )
    }
    return scheduleWeeklyMomentDataList
}

private fun List<Moment>.toScheduleMomentData(): List<ScheduleMomentData> {
    return this.map { moment ->
        ScheduleMomentData(
            title = moment.title,
            timeLabel = formatTime(moment.at),
            iconResource = moment.withIcon,
            withMedicines = moment.withMedicines,
            isCollapsed = moment.isCollapsed
        )
    }
}

private fun formatLabel(date: Date, mode: ScheduleViewMode): String {
    val pattern = when (mode) {
        ScheduleViewMode.Month -> "EEEE dd MMMM"
        ScheduleViewMode.Week -> "MMM dd"
    }
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}

private fun formatTime(date: Date): String {
    val pattern = "HH:mm"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}

private fun formatDay(date: Date): String {
    val pattern = "MMM d"
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
    return "${formatLabel(calendar.time, ScheduleViewMode.Week)} " +
            " - " +
            " ${formatLabel(endCalendar.time, ScheduleViewMode.Week)}"
}
package com.example.mednotes.ui.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mednotes.api.MomentsApi.Companion.GetMoments
import com.example.mednotes.data.Medicine
import com.example.mednotes.data.ScheduleMomentData
import com.example.mednotes.usecase.toMonthlyScheduleMomentsData
import com.example.mednotes.usecase.toWeeklyScheduleMomentsData

class ScheduleViewModel : ViewModel() {

    var scheduleData by mutableStateOf(
        GetMoments.toMonthlyScheduleMomentsData()
    )

    var weeklyData  by mutableStateOf(
        GetMoments.toWeeklyScheduleMomentsData()
    )

    fun updateAllMedicines(
        momentData: ScheduleMomentData,
        isTaken: Boolean) {
        // API Call
        momentData.withMedicines.map{ it.isTaken  = isTaken }
    }


    fun updateMedicine(
        medicine: Medicine,
        isTaken: Boolean) {
        // API Call
        medicine.isTaken = isTaken
    }
}
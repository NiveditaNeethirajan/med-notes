package com.example.mednotes.di

import com.example.mednotes.ui.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ScheduleViewModel() }
}

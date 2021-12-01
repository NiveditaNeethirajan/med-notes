package com.example.mednotes

import android.app.Application
import com.example.mednotes.di.viewModelModule
import org.koin.core.context.startKoin

class MedNotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(viewModelModule)
        }
    }
}

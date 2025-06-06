package com.example.drawai

import android.app.Application
import android.media.VolumeShaper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }
}
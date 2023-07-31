package com.example.testovoe19

import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "875ca7fc-16be-45c5-9053-c0d7ca6fbb18"

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}
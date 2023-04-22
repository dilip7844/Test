package com.example.test

import android.app.Application
import com.google.firebase.FirebaseApp


class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPrefs.initSharedPrefs()
        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var instance: AppConfig
    }

}
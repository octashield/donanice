package br.com.octashield.donanice

import android.app.Application
import com.google.firebase.FirebaseApp

class DonaNiceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
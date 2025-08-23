package io.github.hanihashemi.tomaten

import android.app.Application
import android.os.Build
import co.touchlab.kermit.Logger
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupFirebase()
        setupKoin()
    }

    private fun setupFirebase() {
        try {
            // Check if we're running on an emulator
            val isEmulator =
                Build.FINGERPRINT.startsWith("generic") ||
                    Build.FINGERPRINT.startsWith("unknown") ||
                    Build.MODEL.contains("google_sdk") ||
                    Build.MODEL.contains("Emulator") ||
                    Build.MODEL.contains("Android SDK built for x86") ||
                    Build.MANUFACTURER.contains("Genymotion") ||
                    Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                    "google_sdk" == Build.PRODUCT

            if (isEmulator) {
                Logger.w("Running on emulator - Firebase initialization skipped to avoid Google Play Services issues")
                return
            }

            FirebaseApp.initializeApp(this)
            Logger.d("Firebase initialized successfully")
        } catch (e: Exception) {
            Logger.w("Firebase initialization failed - continuing without Firebase", e)
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}

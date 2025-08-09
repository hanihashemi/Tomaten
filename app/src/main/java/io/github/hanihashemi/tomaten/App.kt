package io.github.hanihashemi.tomaten

import android.app.Application
import android.os.Build
import android.util.Log
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
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
                Timber.w("Running on emulator - Firebase initialization skipped to avoid Google Play Services issues")
                return
            }

            FirebaseApp.initializeApp(this)
            Timber.d("Firebase initialized successfully")
        } catch (e: Exception) {
            Timber.w(e, "Firebase initialization failed - continuing without Firebase")
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?,
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) return

            // Replace with your own crash reporting logic (e.g., Firebase Crashlytics)
            // Here we just print to Logcat as a placeholder
            Log.println(priority, tag, message)

            t?.let {
                when (priority) {
                    Log.ERROR -> Log.e(tag, "CrashReportingTree error", it)
                    Log.WARN -> Log.w(tag, "CrashReportingTree warning", it)
                    else -> {
                        Log.w(tag, "CrashReportingTree warning", it)
                    }
                }
            }
        }
    }
}

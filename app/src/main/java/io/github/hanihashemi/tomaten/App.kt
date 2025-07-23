package io.github.hanihashemi.tomaten

import android.app.Application
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
        FirebaseApp.initializeApp(this)
        setupTimber()
        setupKoin()
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

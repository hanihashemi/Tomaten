package io.github.hanihashemi.tomaten

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules()
        }
    }
}
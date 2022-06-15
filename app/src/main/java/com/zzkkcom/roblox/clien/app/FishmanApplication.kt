package com.zzkkcom.roblox.clien.app

import android.app.Application
import com.zzkkcom.roblox.clien.app.koin.appModule
import com.zzkkcom.roblox.clien.app.koin.seaModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FishmanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@FishmanApplication)
            modules(listOf(appModule, seaModule))
        }
    }
}
package com.zzkkcom.roblox.clien.app.koin

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.SHARED_PREFS_NAME
import com.zzkkcom.roblox.clien.app.utils.security.FishmanSecurityManager
import com.zzkkcom.roblox.clien.app.utils.wrappers.AppsFlyerWrapper
import com.zzkkcom.roblox.clien.app.utils.wrappers.FacebookWrapper
import com.zzkkcom.roblox.clien.app.view_model.FishmanMainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ContentResolver> {
        androidContext().contentResolver
    }
    factory<FishmanSecurityManager> {
        FishmanSecurityManager(contentResolver = get())
    }
    single<FacebookWrapper> {
        FacebookWrapper(context = get())
    }
    single<AppsFlyerWrapper> {
        AppsFlyerWrapper(context = get())
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
    viewModel<FishmanMainViewModel> {
        FishmanMainViewModel(
            app = get(),
            appsFlyerWrapper = get(),
            facebookWrapper = get(),
            sharedPreferences = get()
        )
    }
}
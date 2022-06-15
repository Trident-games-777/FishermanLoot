package com.zzkkcom.roblox.clien.app.utils.wrappers

import android.content.Context
import com.facebook.applinks.AppLinkData
import kotlin.coroutines.suspendCoroutine

class FacebookWrapper(private val context: Context) {
    suspend fun getFacebookData(): String = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context) { data ->
            it.resumeWith(Result.success(data?.targetUri.toString()))
        }
    }
}
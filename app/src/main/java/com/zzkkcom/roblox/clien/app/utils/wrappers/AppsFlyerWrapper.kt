package com.zzkkcom.roblox.clien.app.utils.wrappers

import android.content.ClipData
import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.AF_ID
import kotlin.coroutines.suspendCoroutine

class AppsFlyerWrapper(private val context: Context) {
    init {
        AppsFlyerLib.getInstance().init(AF_ID, null, context)
    }

    suspend fun getAppsData() = suspendCoroutine<MutableMap<String, Any>?> {
        AppsFlyerLib.getInstance()
            .registerConversionListener(context, object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    it.resumeWith(Result.success(data))
                }

                override fun onConversionDataFail(p0: String?) {}
                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
                override fun onAttributionFailure(p0: String?) {}
            })
        AppsFlyerLib.getInstance().start(context)
    }
}
package com.zzkkcom.roblox.clien.app.view_model

import android.app.Application
import android.content.SharedPreferences
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import com.zzkkcom.roblox.clien.app.data.PreLoadedData
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.INITIAL_STR
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.PREF_STR_KEY
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanParams
import com.zzkkcom.roblox.clien.app.utils.wrappers.AppsFlyerWrapper
import com.zzkkcom.roblox.clien.app.utils.wrappers.FacebookWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FishmanMainViewModel(
    app: Application,
    private val appsFlyerWrapper: AppsFlyerWrapper,
    private val facebookWrapper: FacebookWrapper,
    private val sharedPreferences: SharedPreferences,
) : AndroidViewModel(app) {
    private val _loading: MutableLiveData<String> = MutableLiveData()
    val loading = _loading as LiveData<String>

    fun startLoading() {
        val currentStr = sharedPreferences.getString(PREF_STR_KEY, INITIAL_STR)!!
        if (currentStr.contains(INITIAL_STR)) {
            viewModelScope.launch {
                val preLoadedData = preLoadData()
                notify(preLoadedData)
                _loading.postValue(createStrFromData(preLoadedData))
            }
        } else {
            _loading.postValue(currentStr)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun preLoadData(): PreLoadedData {
        return PreLoadedData(
            appsFlyerData = appsFlyerWrapper.getAppsData(),
            facebookData = facebookWrapper.getFacebookData(),
            appsFlyerUid = AppsFlyerLib.getInstance().getAppsFlyerUID(getApplication()),
            googleAdId = withContext(Dispatchers.Default) {
                AdvertisingIdClient
                    .getAdvertisingIdInfo(getApplication()).id.toString()
            }
        )
    }

    private fun createStrFromData(data: PreLoadedData): String {
        return INITIAL_STR.toUri().buildUpon().apply {
            appendQueryParameter(FishmanParams.secure_get_parametr, FishmanParams.secure_key)
            appendQueryParameter(FishmanParams.dev_tmz_key, TimeZone.getDefault().id)
            appendQueryParameter(FishmanParams.gadid_key, data.googleAdId)
            appendQueryParameter(FishmanParams.deeplink_key, data.facebookData)
            appendQueryParameter(
                FishmanParams.source_key,
                data.appsFlyerData?.get("media_source").toString()
            )
            appendQueryParameter(FishmanParams.af_id_key, data.appsFlyerUid)
            appendQueryParameter(
                FishmanParams.adset_id_key,
                data.appsFlyerData?.get("adset_id").toString()
            )
            appendQueryParameter(
                FishmanParams.campaign_id_key,
                data.appsFlyerData?.get("campaign_id").toString()
            )
            appendQueryParameter(
                FishmanParams.app_campaign_key,
                data.appsFlyerData?.get("campaign").toString()
            )
            appendQueryParameter(
                FishmanParams.adset_key,
                data.appsFlyerData?.get("adset").toString()
            )
            appendQueryParameter(
                FishmanParams.adgroup_key,
                data.appsFlyerData?.get("adgroup").toString()
            )
            appendQueryParameter(
                FishmanParams.orig_cost_key,
                data.appsFlyerData?.get("orig_cost").toString()
            )
            appendQueryParameter(
                FishmanParams.af_siteid_key,
                data.appsFlyerData?.get("af_siteid").toString()
            )
        }.toString()
    }

    private fun notify(data: PreLoadedData) {
        OneSignal.initWithContext(getApplication())
        OneSignal.setAppId(FishmanConstants.OS_ID)
        when {
            data.appsFlyerData?.get("campaign")
                .toString() == "null" && data.facebookData == "null" -> {
                OneSignal.sendTag(
                    "key2",
                    "organic"
                )
            }
            data.facebookData != "null" && data.appsFlyerData?.get("campaign")
                .toString() == "null" -> {
                OneSignal.sendTag(
                    "key2",
                    data.facebookData.replace("myapp://", "").substringBefore("/")
                )
            }
            data.appsFlyerData?.get("campaign")
                .toString() != "null" && data.facebookData == "null" -> {
                OneSignal.sendTag(
                    "key2",
                    data.appsFlyerData?.get("campaign").toString().substringBefore("_")
                )
            }
        }
    }
}
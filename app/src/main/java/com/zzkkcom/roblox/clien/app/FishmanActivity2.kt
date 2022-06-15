package com.zzkkcom.roblox.clien.app

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.INITIAL_STR
import com.zzkkcom.roblox.clien.app.utils.constants.FishmanConstants.Companion.PREF_STR_KEY
import com.zzkkcom.roblox.clien.databinding.ActivityFishman2Binding
import org.koin.android.ext.android.inject

class FishmanActivity2 : AppCompatActivity() {
    private var _activityFishman2Binding: ActivityFishman2Binding? = null
    private val activityFishman2Binding: ActivityFishman2Binding get() = _activityFishman2Binding!!
    private val sharedPreferences: SharedPreferences by inject()

    private var messageAb: ValueCallback<Array<Uri?>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFishman2Binding = ActivityFishman2Binding.inflate(layoutInflater)
        setContentView(activityFishman2Binding.root)

        val webView = activityFishman2Binding.wbView
        webView.loadUrl(intent.getStringExtra(INTENT_EXTRA)!!)
        webView.webViewClient = FishmanClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            //For Android API >= 21 (5.0 OS)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = IMAGE_MIME_TYPE
        startActivityForResult(
            Intent.createChooser(intent, IMAGE_CHOOSER_TITLE),
            RESULT_CODE
        )
    }

    private inner class FishmanClient : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {
                Toast.makeText(this@FishmanActivity2, "Error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (sharedPreferences.getString(PREF_STR_KEY, INITIAL_STR)!!.contains(INITIAL_STR)) {
                with(sharedPreferences.edit()) {
                    putString(PREF_STR_KEY, url!!)
                    apply()
                }
            }
        }
    }

    companion object {
        private const val IMAGE_CHOOSER_TITLE = "Image Chooser"
        private const val IMAGE_MIME_TYPE = "image/*"

        private const val RESULT_CODE = 1

        const val INTENT_EXTRA = "extra"
    }
}
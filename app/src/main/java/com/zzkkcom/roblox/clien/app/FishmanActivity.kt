package com.zzkkcom.roblox.clien.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zzkkcom.roblox.clien.app.FishmanActivity2.Companion.INTENT_EXTRA
import com.zzkkcom.roblox.clien.app.utils.security.FishmanSecurityManager
import com.zzkkcom.roblox.clien.app.view_model.FishmanMainViewModel
import com.zzkkcom.roblox.clien.databinding.ActivityFishmanBinding
import com.zzkkcom.roblox.clien.sea.SeaActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FishmanActivity : AppCompatActivity() {
    private var _fishmanBinding: ActivityFishmanBinding? = null
    private val fishmanBinding: ActivityFishmanBinding get() = _fishmanBinding!!

    private val fishmanMainViewModel: FishmanMainViewModel by viewModel()
    private val fishmanSecurityManager: FishmanSecurityManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _fishmanBinding = ActivityFishmanBinding.inflate(layoutInflater)
        setContentView(fishmanBinding.root)

        if (fishmanSecurityManager.secured) {
            startLoading()
            fishmanMainViewModel.loading.observe(this) { loaded ->
                startWebSurfing(loaded)
            }
        } else {
            startLooting()
        }
    }

    private fun startLooting() {
        val intent = Intent(this, SeaActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startWebSurfing(str: String) {
        val intent = Intent(this, FishmanActivity2::class.java)
        intent.putExtra(INTENT_EXTRA, str)
        startActivity(intent)
        finish()
    }

    private fun startLoading() {
        fishmanMainViewModel.startLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        _fishmanBinding = null
    }
}
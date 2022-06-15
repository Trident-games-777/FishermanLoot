package com.zzkkcom.roblox.clien.sea

import android.animation.AnimatorSet
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.zzkkcom.roblox.clien.databinding.ActivitySeaBinding
import org.koin.android.ext.android.inject

class SeaActivity : AppCompatActivity() {
    private var _seaBinding: ActivitySeaBinding? = null
    private val seaBinding: ActivitySeaBinding get() = _seaBinding!!

    private val sea: Sea by inject()
    private lateinit var images: List<ImageView>
    private lateinit var seaAnimations: SeaAnimations
    private var looted = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _seaBinding = ActivitySeaBinding.inflate(layoutInflater)
        setContentView(seaBinding.root)
        images = listOf(
            seaBinding.iv1,
            seaBinding.iv2,
            seaBinding.iv3,
            seaBinding.iv4,
            seaBinding.iv5,
            seaBinding.iv6,
            seaBinding.iv7,
            seaBinding.iv8,
            seaBinding.iv9,
        )
        seaAnimations = SeaAnimations(images)

        seaBinding.buttonSpin.setOnClickListener { button ->
            button.isEnabled = false
            val loot = sea.getShuffledLoot()
            images.forEach { it.setImageResource(loot[images.indexOf(it)]) }

            val diveAnimators = seaAnimations.diveAnimators()
            diveAnimators.doOnEnd {
                val lines: List<List<Int>> = sea.getWinLines(loot)
                if (lines.isNotEmpty()) {
                    val lineAnimators = List(lines.count()) {
                        val anim = seaAnimations.lineAnimation(lines[it])
                        anim.doOnEnd {
                            looted += 3
                            seaBinding.tvLooted.text = looted.toString()
                        }
                        anim
                    }
                    val lineAnimatorSet = AnimatorSet()
                    lineAnimatorSet.playSequentially(lineAnimators)
                    lineAnimatorSet.doOnEnd { button.isEnabled = true }
                    lineAnimatorSet.start()
                } else {
                    button.isEnabled = true
                }

            }
            diveAnimators.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _seaBinding = null
    }
}
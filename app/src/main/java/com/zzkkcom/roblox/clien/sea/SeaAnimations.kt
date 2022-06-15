package com.zzkkcom.roblox.clien.sea

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView

class SeaAnimations(
    private val images: List<ImageView>
) {
    private fun diveAnimator(view: ImageView): Animator {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, 1200f)
        objectAnimator.duration = 500L
        return objectAnimator
    }

    private fun scaleAnimator(view: ImageView): Animator {
        val startScale = 1f
        val endScale = 1.2f
        val startAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", startScale, endScale)
        val startAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", startScale, endScale)
        val startAnimatorSet = AnimatorSet()
        startAnimatorSet.playTogether(startAnimatorX, startAnimatorY)
        startAnimatorSet.duration = 250L

        val endAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", endScale, startScale)
        val endAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", endScale, startScale)
        val endAnimatorSet = AnimatorSet()
        endAnimatorSet.playTogether(endAnimatorX, endAnimatorY)
        endAnimatorSet.duration = 250

        val scaleSet = AnimatorSet()
        scaleSet.playSequentially(startAnimatorSet, endAnimatorSet)
        return scaleSet
    }

    fun diveAnimators(): AnimatorSet {
        val diveAnimations = List(9) { diveAnimator(images[it]) }
        val diveSet = AnimatorSet()
        diveSet.playTogether(diveAnimations)
        return diveSet
    }

    fun lineAnimation(line: List<Int>): AnimatorSet {
        val scaleAnimations = List(3) {
            scaleAnimator(images[line[it]])
        }
        val scaleSet = AnimatorSet()
        scaleSet.playTogether(scaleAnimations)
        return scaleSet
    }
}
package com.example.playlistmaker.player.ui.animations

import android.view.View


class PlayerAnimations {

    fun animateInnerLikeOut(animatedView: View) {
        symmetricalScale(1f, 0.4f, 80, animatedView)

    }

    fun animateInnerLikeIn(animatedView: View) {
        symmetricalScale(0.4f, 1f, 80, animatedView)
    }

    private fun symmetricalScale(from: Float, to: Float, duration: Long, animatedView: View) {
        animatedView.animate().apply {
            this.duration = duration
            scaleX(from)
            scaleY(from)
            withEndAction {
                animatedView.animate().scaleX(to).scaleY(to).start()
            }
        }.start()
    }
}
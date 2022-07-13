package com.treasure.butterfly.toast

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.IntDef
import com.airbnb.lottie.LottieAnimationView
import com.treasure.butterfly.Butterfly
import com.treasure.butterfly.R

/**
 * @author Liangyong Ni
 * @date 2022/7/13
 * description [LottieToast]
 */
class LottieToast(
    @LottieToastType toastType: Int,
    x: Int = 0,
    y: Int = 100,
    gravity: Int = Gravity.BOTTOM,
    @AnimRes animation: Int = R.anim.enter,
    callback: ToastCallback? = null
) : ButterflyToast(toastType, x, y, gravity, animation, callback) {
    companion object {
        const val TOAST_INFO = 0
        const val TOAST_SUCCESS = 1
        const val TOAST_ERROR = 1 shl 1

        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        @IntDef(
            flag = true,
            value = [TOAST_SUCCESS, TOAST_ERROR, TOAST_INFO]
        )
        annotation class LottieToastType
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(@LottieToastType toastType: Int): View {
        val view = LayoutInflater.from(Butterfly.getAppContext()).inflate(R.layout.toast_lottie, null)
        val lottie = view.findViewById<LottieAnimationView>(R.id.lottie)
        when (toastType) {
            TOAST_INFO -> {
                lottie.setAnimation(R.raw.info)
                view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#B0E0E6"))
            }
            TOAST_SUCCESS -> {
                lottie.setAnimation(R.raw.success)
                view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#87CEFA"))
            }
            TOAST_ERROR -> {
                lottie.setAnimation(R.raw.error)
                view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8C69"))
            }
        }
        return view
    }

    override fun getTextView(): TextView {
        return view.findViewById(R.id.tvInfo)
    }
}
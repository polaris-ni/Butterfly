package com.treasure.butterfly.toast

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inspector.WindowInspector
import android.widget.TextView
import androidx.annotation.AnimRes
import com.treasure.butterfly.R
import com.treasure.butterfly.toast.ToastController.getActivity
import com.treasure.butterfly.toast.ToastController.getCurrentTAG
import com.treasure.butterfly.toast.ToastController.register
import com.treasure.butterfly.toast.ToastController.unregister
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Liangyong Ni
 * @date 2022/7/12
 * description [ButterflyToast]
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class ButterflyToast(
    protected val toastType: Int = -1,
    protected val x: Int = 0,
    protected val y: Int = 0,
    protected val gravity: Int = Gravity.BOTTOM,
    @AnimRes protected val animation: Int = R.anim.anim_in,
    protected val callback: ToastCallback? = null
) : IToast {
    protected var isShow: Boolean = false
    protected val view: View by lazy {
        onCreateView(toastType)
    }
    protected val infoTextView:TextView by lazy {
        getTextView()
    }
    protected var text: String? = null
    protected var duration: Long = 2500L

    protected val timer: Timer = Timer()
    protected var params: WindowManager.LayoutParams? = null

    private val mWdm: WeakReference<WindowManager> by lazy {
        WeakReference(getActivity()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
    }

    init {
        params = WindowManager.LayoutParams()
        params?.apply {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSLUCENT
            flags =
                (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            gravity = this@ButterflyToast.gravity
            windowAnimations = animation
            this.x = this@ButterflyToast.x
            this.y = this@ButterflyToast.y
        }
    }

    override fun show(text: String, duration: Long) {
        if (!isShow) {
            this.text = text
            this.duration = duration
            mainHandler.post(showRunnable)
        }
    }

    override fun cancel() {
        if (isShow) {
            mainHandler.post(cancelRunnable)
        }
    }

    override fun clear() {}

    override fun getTag(): String? = getCurrentTAG()

    private val showRunnable: Runnable = Runnable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (WindowInspector.getGlobalWindowViews().contains(view)) {
                mWdm.get()?.removeViewImmediate(view)
            }
        } else {
            if (view.isAttachedToWindow) {
                mWdm.get()?.removeViewImmediate(view)
            }
        }
        infoTextView.text = this.text
        mWdm.get()?.addView(view, params)
        register(this)
        timer.schedule(object : TimerTask() {
            override fun run() {
                this@ButterflyToast.cancel()
            }
        }, duration)
        isShow = true
    }

    private val cancelRunnable: Runnable = Runnable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (WindowInspector.getGlobalWindowViews().contains(view)) {
                mWdm.get()?.removeViewImmediate(view)
            }
        } else {
            if (view.isAttachedToWindow) {
                mWdm.get()?.removeViewImmediate(view)
            }
        }
        callback?.onToastDismiss()
        isShow = false
        timer.cancel()
        params = null
        unregister(this)
    }
}
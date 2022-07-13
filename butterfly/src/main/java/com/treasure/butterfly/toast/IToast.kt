package com.treasure.butterfly.toast

import android.view.View
import android.widget.TextView

/**
 * @author Liangyong Ni
 * @date 2022/7/12
 * description [IToast]
 */
interface IToast {

    fun show(text: String, duration: Long)

    fun cancel()

    fun clear()

    fun getTag(): String?

    fun onCreateView(toastType: Int): View

    fun getTextView(): TextView
}
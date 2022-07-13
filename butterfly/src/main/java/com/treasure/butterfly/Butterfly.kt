package com.treasure.butterfly

import android.app.Application
import android.content.Context
import com.treasure.butterfly.toast.ToastController.activityLifecycle

/**
 * @author Liangyong Ni
 * @date 2022/7/12
 * description [Butterfly]
 */
object Butterfly {
    private lateinit var app: Application

    fun init(app: Application) {
        this.app = app
        app.registerActivityLifecycleCallbacks(activityLifecycle)
    }

    internal fun getAppContext(): Context = app
}
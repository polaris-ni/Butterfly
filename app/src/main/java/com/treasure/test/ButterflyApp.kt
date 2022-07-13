package com.treasure.test

import android.app.Application
import com.treasure.butterfly.Butterfly

/**
 * @author Liangyong Ni
 * @date 2022/7/12
 * description [ButterflyApp]
 */
class ButterflyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Butterfly.init(this)
    }
}
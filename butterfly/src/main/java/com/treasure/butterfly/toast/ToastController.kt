package com.treasure.butterfly.toast

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Liangyong Ni
 * @date 2022/7/12
 * description [ToastController]
 */
object ToastController {

    private var curActivityRef: WeakReference<Activity>? = null
    private val curActivity get() = curActivityRef?.get()

    private val boxMap = HashMap<String, LinkedList<IToast>>()

    val activityLifecycle = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            curActivityRef = WeakReference(activity)
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
            //activity返回
            curActivityRef = WeakReference(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            // 当Activity进入后台时，所有的Toast将会取消显示
            boxMap[activity.toString()]?.forEach {
                it.cancel()
            }
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            // Activity销毁时，清除所有Toast
            activity.toString().let {
                boxMap[it]?.clear()
                boxMap.remove(it)
            }
            if (activity == curActivity) {
                curActivityRef = null
            }
        }
    }

    fun getActivity(): Activity? {
        return curActivity
    }

    fun getCurrentTAG(): String {
        return curActivity.toString()
    }


    @Synchronized
    internal fun register(toast: IToast) {
        val tag = toast.getTag()
        if (tag.isNullOrEmpty()) return

        val linkedList = boxMap[tag] ?: LinkedList()
        boxMap[tag] = linkedList
        linkedList.offer(toast)

        while (linkedList.size > ToastUtil.maxToastSize) {
            linkedList.poll()?.cancel()
        }
    }

    @Synchronized
    internal fun unregister(toast: IToast) {
        val tag = toast.getTag()
        if (tag.isNullOrEmpty()) return
        val linkedList = boxMap[tag] ?: LinkedList()
        boxMap[tag] = linkedList
        toast.clear()
        linkedList.remove(toast)
    }

}
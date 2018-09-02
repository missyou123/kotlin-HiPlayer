package cn.zk.hi.utils

import android.util.DisplayMetrics
import android.content.ComponentCallbacks
import android.app.Application
import android.app.Activity
import android.content.res.Configuration


object DisplayUtils {
    private var sRoncompatDennsity: Float = 0f
    private var sRoncompatScaledDensity: Float = 0f

    /**
     * 设置页面的Density
     */
    fun setCustomDensity(activity: Activity, application: Application) {
        //application
        val appDisplayMetrics = application.resources.displayMetrics

        if (sRoncompatDennsity == 0f) {
            sRoncompatDennsity = appDisplayMetrics.density
            sRoncompatScaledDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {

                }
                override fun onConfigurationChanged(newConfig: Configuration?) {
                    newConfig?.let {
                        if (it.fontScale > 0) {
                            sRoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                        }
                    }
                }
            })
        }

        //计算宽为360dp 同理可以设置高为640dp的根据实际情况
        val originalDp = 360f // 注意此为 widthPx / density 获取的dp值(目标设计图的)
        val targetDensity = appDisplayMetrics.widthPixels / originalDp
        val targetScaledDensity = targetDensity * (sRoncompatScaledDensity / sRoncompatDennsity)
        val targetDensityDpi = (targetDensity * 160).toInt()

        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        appDisplayMetrics.scaledDensity = targetScaledDensity

        //activity
        val activityDisplayMetrics = activity.resources.displayMetrics

        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
        activityDisplayMetrics.scaledDensity = targetScaledDensity
    }

}
package com.example.myapplication3

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.myapplication3.di.component.BaseAppComponent
import com.example.myapplication3.di.component.DaggerBaseAppComponent
import com.example.myapplication3.di.module.BaseAppModule
import com.example.myapplication3.extensions.log

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    fun getBaseComponent() : BaseAppComponent {
        return DaggerBaseAppComponent.builder().baseAppModule(BaseAppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivityStarted(activity: Activity) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivityResumed(activity: Activity) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivityPaused(activity: Activity) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivityStopped(activity: Activity) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    override fun onActivityDestroyed(activity: Activity) {
        activity.log(object {}.javaClass.enclosingMethod?.name ?: "")
    }
}

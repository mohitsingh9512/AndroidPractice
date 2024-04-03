package com.example.myapplication3

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.myapplication3.di.component.BaseAppComponent
import com.example.myapplication3.di.component.DaggerBaseAppComponent
import com.example.myapplication3.di.module.BaseAppModule
import com.example.myapplication3.extensions.log

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    val TAG = "Application"
    fun getBaseComponent() : BaseAppComponent {
        return DaggerBaseAppComponent.builder().baseAppModule(BaseAppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                Log.d(TAG, "onCreate: ")
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                Log.d(TAG, "onStart: ")
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                Log.d(TAG, "onResume: ")
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                Log.d(TAG, "onPause: ")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                Log.d(TAG, "onStop: ")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                Log.d(TAG, "onDestroy: ")
            }
        })
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

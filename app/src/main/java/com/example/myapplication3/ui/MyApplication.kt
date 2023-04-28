package com.example.myapplication3.ui

import android.app.Application
import com.example.myapplication3.di.component.BaseAppComponent
import com.example.myapplication3.di.component.DaggerBaseAppComponent
import com.example.myapplication3.di.module.BaseAppModule

class MyApplication : Application() {

    fun getBaseComponent() : BaseAppComponent {
        return DaggerBaseAppComponent.builder().baseAppModule(BaseAppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}

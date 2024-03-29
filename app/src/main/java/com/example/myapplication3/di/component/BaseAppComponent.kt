package com.example.myapplication3.di.component

import android.app.Application
import android.content.Context
import com.example.myapplication3.di.module.BaseAppModule
import com.example.myapplication3.di.scope.ApplicationScope
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@ApplicationScope
@Component(modules = [BaseAppModule::class])
interface BaseAppComponent {

    fun getApplicationContext() : Context

    fun getRetrofit() : Retrofit

    fun getApplication() : Application
}
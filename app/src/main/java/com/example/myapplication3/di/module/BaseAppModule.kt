package com.example.myapplication3.di.module

import android.app.Application
import android.content.Context
import com.example.myapplication3.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [BaseRetrofitModule::class])
class BaseAppModule(var application : Application) {

    @Provides
    @ApplicationScope
    fun providesApplication() : Application {
        return application
    }

    @Provides
    @ApplicationScope
    fun providesApplicationContext() : Context {
        return application.applicationContext
    }
}
package com.example.myapplication3.di.component

import com.example.myapplication3.di.module.AppModule
import com.example.myapplication3.di.module.ViewModelModule
import com.example.myapplication3.di.scope.AppScope
import com.example.myapplication3.ui.MainFragment
import com.example.myapplication3.ui.SecondFragment
import dagger.Component


// Application consists of Functional Set and Construction Set + Integration
// DI framework helps in cons set and integration


// Convention over Configuration


@AppScope
@Component(
    modules = [AppModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface AppComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: SecondFragment)

}
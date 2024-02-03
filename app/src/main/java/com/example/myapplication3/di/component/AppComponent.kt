package com.example.myapplication3.di.component

import com.example.myapplication3.di.module.AppModule
import com.example.myapplication3.di.module.ViewModelModule
import com.example.myapplication3.di.scope.AppScope
import com.example.myapplication3.ui.MainFragment
import com.example.myapplication3.ui.SecondFragment
import dagger.Component

@AppScope
@Component(
    modules = [AppModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface AppComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: SecondFragment)

}
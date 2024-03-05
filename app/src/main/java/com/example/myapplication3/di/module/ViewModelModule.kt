package com.example.myapplication3.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication3.di.component.ViewModelProviderFactory
import com.example.myapplication3.di.scope.ViewModelKey
import com.example.myapplication3.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(modelProvider: ViewModelProviderFactory) :ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun providesMainViewModel(mainViewModel: MainViewModel) : ViewModel
}



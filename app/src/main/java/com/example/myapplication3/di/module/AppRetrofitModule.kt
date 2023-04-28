package com.example.myapplication3.di.module

import com.example.myapplication3.network.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class AppRetrofitModule {

    @Provides
    fun providesApiInterface(retrofit: Retrofit) : ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}

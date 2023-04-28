package com.example.myapplication3.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class BaseRetrofitModule {

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient,
                    gsonConverterFactory: GsonConverterFactory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://go.com/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun providesGsonConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }
}

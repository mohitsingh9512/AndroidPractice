package com.example.myapplication3.di.module

import com.example.myapplication3.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier

@Module
class BaseRetrofitModule {

    @Provides
    @ApplicationScope
    fun providesRetrofit(@HttpClientTimeOut okHttpClient: OkHttpClient,
                    gsonConverterFactory: GsonConverterFactory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://go.com/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @ApplicationScope
    fun providesGsonConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @HttpClientTimeOut
    fun providesOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @HttpClientNoTimeOut
    fun providesOkHttpClientSafe() : OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}

@Qualifier
annotation class HttpClientTimeOut{

}

@Qualifier
annotation class HttpClientNoTimeOut{

}
package com.example.myapplication3.network

import okhttp3.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("data")
    suspend fun getData() : Response
}

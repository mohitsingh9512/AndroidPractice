package com.example.myapplication3.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class SampleCoroutine {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun getMovies() {
        coroutineScope.launch {
            getFromAPI()
            getFromNetwork()
            getFromDB()
            launch {
                apicall()
            }
            launch {
                apicall()
            }
        }
    }

    suspend fun getFromAPI() {

    }

    suspend fun getFromNetwork() {

    }

    suspend fun getFromDB() {

    }

    private fun apicall() {

    }

}

fun main() {
    SampleCoroutine().getMovies()
}
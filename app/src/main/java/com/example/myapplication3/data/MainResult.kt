package com.example.myapplication3.data

sealed class MainResult {
    class Success(val listEmployees : ArrayList<BaseDataModel>) : MainResult()
    class Fail(throwable: Throwable) : MainResult()
    object Loading : MainResult()
}

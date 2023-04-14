package com.example.myapplication3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication3.data.MainResult
import com.example.myapplication3.usecase.EmployeesUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() , CoroutineScope{

    private val _employeeList = MutableLiveData<MainResult>()
    val employeesList = _employeeList

    private val coroutineScope = CoroutineScope(coroutineContext)

    fun getData(){
        //TODO Inject
        coroutineScope.launch {
            val result : Deferred<MainResult>  = async {
                EmployeesUseCase().getData()
            }
            val response = result.await()
            _employeeList.value = response
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
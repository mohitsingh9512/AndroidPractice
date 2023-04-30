package com.example.myapplication3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication3.data.MainResult
import com.example.myapplication3.usecase.EmployeesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel @Inject constructor(private val employeesUseCase: EmployeesUseCase) : ViewModel() , CoroutineScope{

    private val _employeeList = MutableLiveData<MainResult>()
    val employeesList : LiveData<MainResult> = _employeeList

    private val _employeeListFlow = MutableStateFlow<MainResult>(MainResult.Loading)
    val employeeListFlow : StateFlow<MainResult> = _employeeListFlow

    private val coroutineScope = CoroutineScope(coroutineContext)

    fun getData(){
        coroutineScope.launch {
            val result : Deferred<MainResult>  = async {
                employeesUseCase.getData()
            }
            val response = result.await()
            _employeeList.value = response
        }
    }

    fun getDataFlow(){
        coroutineScope.launch {
            employeesUseCase.getDataFlow()
                .catch {

                }.collect {
                    _employeeListFlow.value = it
                }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
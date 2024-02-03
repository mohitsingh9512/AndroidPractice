package com.example.myapplication3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication3.data.MainResult
import com.example.myapplication3.liveData.SingleLiveData
import com.example.myapplication3.usecase.EmployeesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.annotation.concurrent.ThreadSafe
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel @Inject constructor(private val employeesUseCase: EmployeesUseCase) : ViewModel() , CoroutineScope{

    private val _employeeList = MutableLiveData<MainResult>()
    val employeesList : LiveData<MainResult> = _employeeList

    private val _employeeListFlow = MutableStateFlow<MainResult>(MainResult.Loading)
    val employeeListFlow : StateFlow<MainResult> = _employeeListFlow

    private val _click = SingleLiveData<Int>()

    private val coroutineScope = CoroutineScope(coroutineContext)

    init {
        getDataFlow()
    }

    private fun getData(){
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
                    _employeeListFlow.emit(MainResult.Fail(Throwable("Exception occurred")))
                }.collect {
                    _employeeListFlow.emit(it)
                }
        }
    }

    fun sendClickEvents() {
        Log.d("TAG", "sendClickEvents: ${Thread.currentThread()}")
        launch(Dispatchers.Main) {
            Log.d("TAG", "sendClickEvents: ${Thread.currentThread()}")
            delay(2000)
            _click.setValue(1)
            withContext(Dispatchers.IO){
                Log.d("TAG", "sendClickEvents: ${Thread.currentThread()}")
                delay(2000)
                _click.setValue(2)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
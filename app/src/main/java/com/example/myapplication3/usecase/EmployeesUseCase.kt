package com.example.myapplication3.usecase

import com.example.myapplication3.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesUseCase @Inject constructor() {

    suspend fun getData() : MainResult {
        return withContext(Dispatchers.IO){
            if(true) {
                val depts = arrayListOf(EmployeesResponse.Employees.Dept(20,"Dept1"), EmployeesResponse.Employees.Dept(20,"Dept2"))
                val employee1 = EmployeesResponse.Employees(dept = depts , "11", "Rohit", "0" , "P")
                val employee2 = EmployeesResponse.Employees(dept = depts , "11", "Rohit", "0" , "P")
                val employee3 = EmployeesResponse.Employees(dept = null , "10", "Mohit", "0" , "P")
                val arrayList = arrayListOf<EmployeesResponse.Employees>()
                arrayList.add(employee1)
                arrayList.add(employee2)
                arrayList.add(employee3)
//                for(i in 1..100){
//                    arrayList.add(EmployeesResponse.Employees(dept = null , "1", "$i", "0" , "P"))
//                }

                return@withContext MainResult.Success(processResult(arrayList))
            }else {
                return@withContext MainResult.Fail(Throwable("Api Fail"))
            }
        }
    }

    suspend fun getDataFlow() : Flow<MainResult> {
        return flow {
            val depts = arrayListOf(EmployeesResponse.Employees.Dept(20,"Dept1"), EmployeesResponse.Employees.Dept(20,"Dept2"))
            val employee1 = EmployeesResponse.Employees(dept = depts , "1", "Rohit", "0" , "P")
            val employee2 = EmployeesResponse.Employees(dept = depts , "1", "Rohit", "0" , "P")
            val employee3 = EmployeesResponse.Employees(dept = null , "1", "Rohit", "0" , "P")
            val employee4 = EmployeesResponse.Employees(dept = null , "1", "Mohit", "0" , "P")

            val arrayList = arrayListOf<EmployeesResponse.Employees>()
            for(i in 0..100){
                arrayList.add(EmployeesResponse.Employees(dept = null , "1", "$i", "0" , "P"))
            }
            emit(arrayList)
        }.map {
            MainResult.Success(processResult(it))
        }
//        transform {
//            emit(MainResult.Success(processResult(it)))
//        }
        .flowOn(Dispatchers.IO)
    }

    private fun processResult(arrayList: ArrayList<EmployeesResponse.Employees>): ArrayList<BaseDataModel> {
        val arrayListDataModel = arrayListOf<BaseDataModel>()
        for (obj in  arrayList){
            if(obj.dept == null){
                arrayListDataModel.add(EmployeeDataModel(obj))
            }else {
                arrayListDataModel.add(ContainerDataModel(
                    processDepartmentDataModel(obj.dept)
                ))
            }
        }
        return arrayListDataModel
    }

    private fun processDepartmentDataModel(departments : List<EmployeesResponse.Employees.Dept>): ArrayList<BaseDataModel> {
        val arrayList = arrayListOf<BaseDataModel>()
        for(d in departments){
            arrayList.add(DepartmentDataModel(d))
        }
        return arrayList
    }
}
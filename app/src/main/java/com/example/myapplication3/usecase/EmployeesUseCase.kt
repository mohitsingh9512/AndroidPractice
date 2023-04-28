package com.example.myapplication3.usecase

import com.example.myapplication3.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesUseCase @Inject constructor() {

    suspend fun getData() : MainResult {
        return withContext(Dispatchers.IO){
            if(true) {
                val depts = arrayListOf(EmployeesResponse.Employees.Dept(20,"Dept1"), EmployeesResponse.Employees.Dept(20,"Dept2"))
                val employee1 = EmployeesResponse.Employees(dept = depts , "1", "Rohit", "0" , "P")
                val employee2 = EmployeesResponse.Employees(dept = depts , "1", "Rohit", "0" , "P")
                val employee3 = EmployeesResponse.Employees(dept = null , "1", "Rohit", "0" , "P")
                val employee4 = EmployeesResponse.Employees(dept = null , "1", "Mohit", "0" , "P")
                val arrayList = arrayListOf(employee1, employee2, employee3,employee4)

                return@withContext MainResult.Success(processResult(arrayList))
            }else {
                return@withContext MainResult.Fail(Throwable("Api Fail"))
            }
        }
    }

    private fun processResult(arrayList: ArrayList<EmployeesResponse.Employees>): ArrayList<BaseDataModel> {
        val arrayListDataModel = arrayListOf<BaseDataModel>()
        for (obj in  arrayList){
            if(obj.dept == null){
                arrayListDataModel.add(EmployeeDataModel(obj))
            }else {
                arrayListDataModel.add(ContainerDataModel(
                    processDeptartmentDataModel(obj.dept)
                ))
            }
        }
        return arrayListDataModel
    }

    private fun processDeptartmentDataModel(depts: List<EmployeesResponse.Employees.Dept>): ArrayList<BaseDataModel> {
        val arrayList = arrayListOf<BaseDataModel>()
        for(d in depts){
            arrayList.add(DepartmentDataModel(d))
        }
        return arrayList
    }
}
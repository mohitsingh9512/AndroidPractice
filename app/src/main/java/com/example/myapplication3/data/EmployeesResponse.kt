package com.example.myapplication3.data

data class EmployeesResponse(
    val list: List<Employees>
) {
    data class Employees(
        val dept: List<Dept>?,
        val id: String?,
        val name: String?,
        val salary: String?,
        val type: String?
    ) {
        data class Dept(
            val empCount: Int?,
            val name: String?
        )
    }
}
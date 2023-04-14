package com.example.myapplication3.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.myapplication3.R
import com.example.myapplication3.data.BaseDataModel
import com.example.myapplication3.data.EmployeeDataModel
import com.example.myapplication3.data.EmployeesResponse

class EmployeeViewHolder(view: View) : AbstractViewHolder<EmployeeDataModel>(view) {

    companion object {
        @LayoutRes
        const val LAYOUT = R.layout.item_view_employee
    }

    private val title : TextView by lazy {
        view.findViewById(R.id.name)
    }

    override fun bind(model: EmployeeDataModel) {
        title.text = model.employee.name
    }

}
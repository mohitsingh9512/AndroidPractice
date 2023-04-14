package com.example.myapplication3.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.myapplication3.R
import com.example.myapplication3.data.BaseDataModel
import com.example.myapplication3.data.DepartmentDataModel
import com.example.myapplication3.data.EmployeesResponse

class DepartmentViewHolder(view: View) : AbstractViewHolder<DepartmentDataModel>(view){

    companion object {
        @LayoutRes
        const val LAYOUT = R.layout.item_view_dept
    }

    private val textView : TextView by lazy {
        view.findViewById(R.id.dept_name)
    }

    override fun bind(model: DepartmentDataModel) {
        textView.text = model.dept.name
    }

}
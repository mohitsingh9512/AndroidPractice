package com.example.myapplication3.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication3.R
import com.example.myapplication3.data.BaseDataModel
import com.example.myapplication3.viewholder.AbstractViewHolder
import com.example.myapplication3.viewholder.ContainerViewHolder
import com.example.myapplication3.viewholder.DepartmentViewHolder
import com.example.myapplication3.viewholder.EmployeeViewHolder

class NormalRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var dataList: ArrayList<BaseDataModel>? = null
    fun submitList(arrayList: ArrayList<BaseDataModel>){
        dataList = arrayList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            R.layout.item_view_dept -> DepartmentViewHolder(parent)
            R.layout.item_view_employee -> EmployeeViewHolder(parent)
            else -> ContainerViewHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { (holder as? AbstractViewHolder<BaseDataModel>)?.bind(it) }
    }
}
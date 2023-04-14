package com.example.myapplication3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.data.BaseDataModel
import com.example.myapplication3.data.ContainerDataModel
import com.example.myapplication3.data.DepartmentDataModel
import com.example.myapplication3.data.EmployeeDataModel
import com.example.myapplication3.viewholder.AbstractViewHolder
import com.example.myapplication3.viewholder.ContainerViewHolder
import com.example.myapplication3.viewholder.DepartmentViewHolder
import com.example.myapplication3.viewholder.EmployeeViewHolder

class MainAdapter : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private var list = ArrayList<BaseDataModel>()

    fun submitList(l : ArrayList<BaseDataModel>){
        list = l
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view  = createViewItem(parent,viewType)
        return when(viewType) {
            EmployeeViewHolder.LAYOUT -> EmployeeViewHolder(view)
            ContainerViewHolder.LAYOUT -> ContainerViewHolder(view)
            DepartmentViewHolder.LAYOUT -> DepartmentViewHolder(view)
            else -> DepartmentViewHolder(view)
        }
    }

    private fun createViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType,parent,false)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseDataModel> , position)
    }

    private fun bind(holder: AbstractViewHolder<BaseDataModel>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]){
            is EmployeeDataModel -> EmployeeViewHolder.LAYOUT
            is ContainerDataModel -> ContainerViewHolder.LAYOUT
            is DepartmentDataModel -> DepartmentViewHolder.LAYOUT
            else -> DepartmentViewHolder.LAYOUT
        }
    }
}

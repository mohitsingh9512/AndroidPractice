package com.example.myapplication3.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.R
import com.example.myapplication3.adapter.MainAdapter
import com.example.myapplication3.data.BaseDataModel
import com.example.myapplication3.data.ContainerDataModel

class ContainerViewHolder(view: View) : AbstractViewHolder<ContainerDataModel>(view){

    companion object {
        @LayoutRes
        const val LAYOUT = R.layout.item_view_container
    }

    private var mainAdapter : MainAdapter? = null

    private val recyclerView : RecyclerView by lazy {
        view.findViewById(R.id.dept_rv)
    }

    override fun bind(model: ContainerDataModel) {
        setupAdapter(model)
    }

    private fun setupAdapter(model: ContainerDataModel) {
        mainAdapter = MainAdapter()
        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
            adapter = mainAdapter
            mainAdapter?.submitList(model.list)
        }
    }
}
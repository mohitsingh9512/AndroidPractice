package com.example.myapplication3.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.data.BaseDataModel

abstract class AbstractViewHolder<in Model>(view : View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: Model)
}
package com.example.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 22:22
 */
abstract class MyAdapter<T>(
    private val context: Context?,
    private val layoutId: Int,
    private val list: List<T>
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return MyViewHolder(view, context!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bindViewHolder(holder, list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    abstract fun bindViewHolder(holder: MyViewHolder, data: T, position: Int)
}
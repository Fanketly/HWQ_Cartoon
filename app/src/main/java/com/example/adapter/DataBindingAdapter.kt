package com.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/22
 * Time: 15:22
 */
class DataBindingAdapter<T>(
    private val list: List<T>,
    private val varId: Int,
    @LayoutRes private val layId: Int
) :
    RecyclerView.Adapter<DataBindingAdapter.ViewHolder>() {
    private var mark = false
    private val onclickMap = LinkedHashMap<Int, (p: Int, t: T) -> Unit>()

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var view: (p: Int, t: T, v: View) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layId,
                parent,
                false
            )
        )

    fun setOnClick(id: Int, onclick: (p: Int, t: T) -> Unit) {
        if (onclickMap.containsKey(id)) return
        onclickMap[id] = onclick
    }

    fun getView(v: (p: Int, t: T, v: View) -> Unit) {
        mark = true
        view = v
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(varId, list[position])
        val view=holder.binding.root
        for (entry in onclickMap) {
            view.findViewById<View>(entry.key).setOnClickListener { entry.value.invoke(position,list[position]) }
        }
        if (mark) view(position,list[position],view)
    }

    override fun getItemCount(): Int = list.size

}
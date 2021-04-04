package com.example.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 19:41
 */
abstract class ViewBindingRvAdapter<D, T : ViewBinding>(private val list: List<D>) :
    RecyclerView.Adapter<ViewBindingRvAdapter.VH<T>>() {
    protected inline fun <reified VB : T> viewBinding(
        viewGroup: ViewGroup
    ): VH<VB> {
        return VH(
            VB::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                .invoke(null, LayoutInflater.from(viewGroup.context), viewGroup, false) as VB
        )
    }

    override fun onBindViewHolder(holder: VH<T>, position: Int) =
        onBind(holder.b, list[position], position)

    override fun getItemCount(): Int = list.size

    class VH<T : ViewBinding>(val b: T) : RecyclerView.ViewHolder(b.root)

    protected abstract fun onBind(b: T, d: D, p: Int)

    protected lateinit var onclick: (p: Int) -> Unit

    fun setOnClick(onclick: (p: Int) -> Unit) {
        this.onclick = onclick
    }
}
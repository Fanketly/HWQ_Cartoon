package com.example.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 22:28
 */
class MyViewHolder(itemView: View, val context: Context) :
    RecyclerView.ViewHolder(itemView) {
    private val sparseArray: SparseArray<View> = SparseArray()


    fun <T : View?> findViewById(id: Int): T {
        var view: View? = sparseArray.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            sparseArray.put(id, view)
        }
        return view as T
    }
}
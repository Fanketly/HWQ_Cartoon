package com.example.base

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/2
 * Time: 18:26
 */
fun <T> RecyclerView.setUpWithLinear(t: T) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    adapter = t as RecyclerView.Adapter<*>
}

fun <T> RecyclerView.setUpWithGrid(t: T, num: Int) {
    layoutManager = GridLayoutManager(context, num)
    adapter = t as RecyclerView.Adapter<*>
}
const val TAG="TAG"



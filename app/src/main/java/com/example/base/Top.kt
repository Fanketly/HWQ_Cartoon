package com.example.base

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.Headers
import java.util.HashMap

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
const val Url3 = "https://manhua.dmzj.com/"
val headers = Headers {
    val map: MutableMap<String, String> =
        HashMap()
    map["Referer"] = "https://manhua.dmzj.com/update_1.shtml"
    map
}

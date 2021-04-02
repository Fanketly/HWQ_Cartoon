package com.example.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
fun <T> RecyclerView.setUpWithLinearHORIZONTAL(t: T) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = t as RecyclerView.Adapter<*>
}
fun <T> RecyclerView.setUpWithGrid(t: T, num: Int) {
    layoutManager = GridLayoutManager(context, num)
    adapter = t as RecyclerView.Adapter<*>
}

const val TAG = "TAG"

// val headers = Headers {
//    val map: MutableMap<String, String> =
//        HashMap()
//    map["Referer"] = "https://manhua.dmzj.com/update_1.shtml"
//    map
//}
@BindingAdapter("setImg")
fun setImg(imageView: ImageView, url: Any) {
    Glide.with(imageView).asDrawable().skipMemoryCache(true).load(url).into(imageView)
}


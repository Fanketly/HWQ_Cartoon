package com.example.hwq_cartoon

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/2
 * Time: 18:26
 */
fun <T> RecyclerView.setUpWithLinear(
    t: T,
    orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false
) {
    layoutManager = LinearLayoutManager(context, orientation, reverse)
    adapter = t as RecyclerView.Adapter<*>
}

fun <T> RecyclerView.setUpWithGrid(
    t: T,
    num: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false
) {
    layoutManager = GridLayoutManager(context, num, orientation, reverse)
    adapter = t as RecyclerView.Adapter<*>
}

const val TAG = "TAG"

const val AUTO = "auto"
const val THEME = "blackTheme"
const val PAGER_ORIENTATION = "pagerorientation"


@BindingAdapter("setImg")
fun setImg(imageView: ImageView, url: String) {
    imageView.load(url)
}


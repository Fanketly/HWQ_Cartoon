package com.example.util

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
fun RecyclerView.setUpWithLinear(
    t: RecyclerView.Adapter<*>,
    orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false
) {
    layoutManager = LinearLayoutManager(context, orientation, reverse)
    adapter = t
}

fun RecyclerView.setUpWithGrid(
    t: RecyclerView.Adapter<*>,
    num: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false
) {
    layoutManager = GridLayoutManager(context, num, orientation, reverse)
    adapter = t
}

const val TAG = "TAG"

const val AUTO = "auto"
const val THEME = "blackTheme"
const val PAGER_ORIENTATION = "pagerorientation"


@BindingAdapter("setImg")
fun setImg(imageView: ImageView, url: String) {
    imageView.load(url)
}


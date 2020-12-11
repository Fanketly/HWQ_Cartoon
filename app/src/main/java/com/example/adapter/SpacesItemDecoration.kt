package com.example.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/10
 * Time: 15:00
 */
class SpacesItemDecoration(val space:Int) : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom=space
    }
}
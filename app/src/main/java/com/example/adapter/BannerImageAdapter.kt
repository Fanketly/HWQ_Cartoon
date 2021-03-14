package com.example.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 16:30
 */
class BannerImageAdapter(val list: List<Int>) : BannerAdapter<Int, BannerImageAdapter.VH>(list) {
    class VH(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): VH {
        val imageView = ImageView(parent?.context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return VH(imageView)
    }

    override fun onBindView(
        holder: VH,
        data: Int?,
        position: Int,
        size: Int
    ) {
        Glide.with(holder.imageView).asDrawable().skipMemoryCache(true).load(list[position])
            .into(holder.imageView)
    }

}
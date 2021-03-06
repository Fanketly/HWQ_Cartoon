package com.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hwq_cartoon.R
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 16:30
 */
class BannerImageAdapter(val list: List<Int>) : BannerAdapter<Int, BannerImageAdapter.VH>(list) {
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView2)
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent?.context).inflate(R.layout.rv_item_banner, parent, false)
        )
    }

    override fun onBindView(
        holder: VH,
        data: Int?,
        position: Int,
        size: Int
    ) {
        holder.imageView.load(data!!)
//        Glide.with(holder.imageView).asDrawable().skipMemoryCache(true).load(data)
//            .centerCrop()
//            .into(holder.imageView)
    }

}
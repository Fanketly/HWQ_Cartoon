package com.example.detailed

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
import com.example.hwq_cartoon.R
import com.github.chrisbanes.photoview.PhotoView

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 21:49
 */
class CartoonImgRvAdapter(context: Context, list: List<ByteArray>) : MyAdapter<ByteArray>(
    context, R.layout.cartoon_img_rv_item, list
) {

    override fun bindViewHolder(holder: MyViewHolder, data: ByteArray, position: Int) {
        val imageView:PhotoView = holder.findViewById(R.id.imageView)
        imageView.setOnClickListener { onClick() }
        Glide.with(holder.context).asDrawable().load(data).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
    }

    private lateinit var onClick: () -> Unit
    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }
}
package com.example.adapter

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.CartoonImgRvItemBinding

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 9:29
 */
class DetailImgRvAdapter(list: List<String>) :
    ViewBindingRvAdapter<String, CartoonImgRvItemBinding>(list) {

    override fun onBind(b: CartoonImgRvItemBinding, d: String, p: Int) {
        b.imageView.setOnClickListener { onclick(p) }
        Glide.with(b.imageView).asDrawable().load(d)
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(b.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<CartoonImgRvItemBinding> {
        return viewBinding(parent)
    }

}
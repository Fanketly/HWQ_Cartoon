package com.example.adapter

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.CartoonRvItemBinding
import com.example.repository.model.CartoonInfo

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 20:18
 */
class HomeRvAdapter(list: List<CartoonInfo>) :
    ViewBindingRvAdapter<CartoonInfo, CartoonRvItemBinding>(list) {
    //    lateinit var img: ImageView
    override fun onBind(b: CartoonRvItemBinding, d: CartoonInfo, p: Int) {
        b.tvCartoonTitle.text = d.title
        b.root.setOnClickListener {
            onclick(
                p
            )
//            if (p == 0) {
//                b.imgCartoon.transitionName = "mark"
//                img = b.imgCartoon
//            }
        }
        if (d.img.isNotEmpty()) {
            Glide.with(b.imgCartoon).asDrawable().load(d.img)
                .skipMemoryCache(true).into(b.imgCartoon)
        }
        b.tvCartoonType.text = d.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<CartoonRvItemBinding> {
        return viewBinding(parent)
    }


}
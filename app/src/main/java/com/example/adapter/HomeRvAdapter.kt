package com.example.adapter

import android.view.ViewGroup
import coil.load
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
        b.tvCartoonType.text = d.type
        if (d.img.isEmpty()) {
            return
//            Glide.with(b.imgCartoon).asDrawable().load(d.img)
//                .skipMemoryCache(true).into(b.imgCartoon)
        }
        b.imgCartoon.load(d.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<CartoonRvItemBinding> {
        return viewBinding(parent)
    }


}
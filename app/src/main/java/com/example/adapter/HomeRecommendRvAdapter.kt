package com.example.adapter

import android.view.ViewGroup
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.RvItemHomeRecommendBinding
import com.example.repository.model.CartoonInfo

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 20:37
 */
class HomeRecommendRvAdapter(list: List<CartoonInfo>) :
    ViewBindingRvAdapter<CartoonInfo, RvItemHomeRecommendBinding>(list) {
    override fun onBind(b: RvItemHomeRecommendBinding, d: CartoonInfo, p: Int) {
        b.tvCartoonTitle.text = d.title
        b.root.setOnClickListener {
            onclick(
                p
            )
        }
        if (d.img.isEmpty()) return
        b.imgCartoon.load(d.img)
//            Glide.with(b.imgCartoon).asDrawable().load(d.img)
//                .skipMemoryCache(true).into(b.imgCartoon)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH<RvItemHomeRecommendBinding> {
        return viewBinding(parent)
    }


}
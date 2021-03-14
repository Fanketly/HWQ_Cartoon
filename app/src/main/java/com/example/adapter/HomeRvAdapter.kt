package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.hwq_cartoon.databinding.CartoonRvItemBinding
import com.example.repository.model.CartoonInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 20:18
 */
class HomeRvAdapter(list: List<CartoonInfor>) :
    ViewBindingRvAdapter<CartoonInfor, CartoonRvItemBinding>(list) {
    override fun onBind(b: CartoonRvItemBinding, d: CartoonInfor, p: Int) {
        b.tvCartoonTitle.text = d.titile
        b.root.setOnClickListener {
            onClick(
                p
            )
        }
        if (d.img != null && d.img.isNotEmpty()) {
            Glide.with(b.imgCartoon).asDrawable().load(d.img)
                .skipMemoryCache(true).into(b.imgCartoon)
        }
        b.tvCartoonType.text = d.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<CartoonRvItemBinding> {
        return VH(viewBinding(LayoutInflater.from(parent.context),parent,false))
    }

    private lateinit var onClick: (p: Int) -> Unit
    fun setOnClick(onClick: (p: Int) -> Unit) {
        this.onClick = onClick
    }
}
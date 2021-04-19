package com.example.adapter

import android.view.ViewGroup
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.RvItemFavouriteBinding
import com.example.repository.model.FavouriteInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 10:55
 */
class FavouriteRvAdapter(list: List<FavouriteInfor>) :
    ViewBindingRvAdapter<FavouriteInfor, RvItemFavouriteBinding>(list) {
    override fun onBind(b: RvItemFavouriteBinding, d: FavouriteInfor, p: Int) {
        b.tvCartoonTitle.text = d.title
        with(b.root) {
            setOnClickListener { onclick(p) }
            setOnLongClickListener {
                longOnclick(p)
                false
            }
        }
        if (d.imgUrl.isNullOrEmpty()) return
        b.imgCartoon.load(d.imgUrl)
//        Glide.with(b.imgCartoon).asDrawable().load(d.imgUrl).skipMemoryCache(true)
//            .into(b.imgCartoon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<RvItemFavouriteBinding> {
        return viewBinding(parent)
    }

    private lateinit var longOnclick: (p: Int) -> Unit
    fun setOnClick(onclick: (p: Int) -> Unit, longOnclick: (p: Int) -> Unit) {
        this.onclick = onclick
        this.longOnclick = longOnclick
    }
}
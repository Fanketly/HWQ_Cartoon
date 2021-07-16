package com.example.adapter

import android.util.Log
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
    override fun onBind(b: CartoonRvItemBinding, d: CartoonInfo, p: Int) {
        b.tvCartoonTitle.text = d.title
        b.root.setOnClickListener {
            onclick(
                p
            )
        }
        b.tvCartoonType.text = d.type
        if (d.img.isEmpty()) {
            return
        }
        Log.i("TAG", "CartoonViewModel_pager:${d.img} ")
        b.imgCartoon.load(d.img){
            setHeader("Referer","https://manhua.dmzj.com/update_1.shtml")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<CartoonRvItemBinding> {
        return viewBinding(parent)
    }


}
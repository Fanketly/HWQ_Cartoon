package com.example.adapter

import android.util.Log
import android.view.ViewGroup
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.RvItemCartoonBinding
import com.example.repository.model.CartoonInfo

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 20:18
 */
class HomeRvAdapter(list: List<CartoonInfo>) :
    ViewBindingRvAdapter<CartoonInfo, RvItemCartoonBinding>(list) {
    override fun onBind(b: RvItemCartoonBinding, d: CartoonInfo, p: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<RvItemCartoonBinding> {
        return viewBinding(parent)
    }


}
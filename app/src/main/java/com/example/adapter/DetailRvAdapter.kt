package com.example.adapter

import android.view.ViewGroup
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.CartoonDialogRvItemBinding
import com.example.repository.model.CartoonInfo

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 10:38
 */
class DetailRvAdapter(list: List<CartoonInfo>, private var mark: Int) :
    ViewBindingRvAdapter<CartoonInfo, CartoonDialogRvItemBinding>(list) {

    override fun onBind(b: CartoonDialogRvItemBinding, d: CartoonInfo, p: Int) {

        with(b.tvCartoonDialogTitle) {
            if (p == mark) {
                setBackgroundResource(R.drawable.text_boder_radius_blue)
//                setTextColor(ContextCompat.getColor(this.context, R.color.black))
            } else {
                setBackgroundResource(R.drawable.text_boder_radius)
//                setTextColor(ContextCompat.getColor(this.context, R.color.white))
            }
                text = d.title
            }
            b.root.setOnClickListener { onclick(p) }
        }

        fun itemChange(mark: Int) {
            val mark2 = this.mark
            this.mark = mark
            notifyItemChanged(mark2)
            notifyItemChanged(mark)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): VH<CartoonDialogRvItemBinding> {
            return viewBinding(parent)
        }
    }
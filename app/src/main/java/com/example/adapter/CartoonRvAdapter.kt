package com.example.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 22:01
 */
class CartoonRvAdapter(list: MutableList<CartoonInfor>, context: Context) : MyAdapter<CartoonInfor>(
    context,
    R.layout.cartoon_rv_item, list
) {
    override fun bindViewHolder(holder: MyViewHolder, data: CartoonInfor, position: Int) {
        val title = holder.findViewById<TextView>(R.id.tvCartoonTitle)
        title.text = data.titile
        holder.itemView.setOnClickListener {
            onClick(
                position
            )
        }
        val type = holder.findViewById<TextView>(R.id.tvCartoonType)
        type.text = data.type
        val imageView = holder.findViewById<ImageView>(R.id.imgCartoon)
        if (data.img != null && data.img.isNotEmpty()) {
            Glide.with(holder.context).asDrawable().load(data.img)
                .skipMemoryCache(true).into(imageView)
        }
    }

    private lateinit var onClick: (p: Int) -> Unit
    fun setOnClick(onClick: (p: Int) -> Unit) {
        this.onClick = onClick
    }
}
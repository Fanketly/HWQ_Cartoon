package com.example.favourite

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 21:29
 */
class FavouriteRvAdapter(list: MutableList<FavouriteInfor>, context: Context) :
    MyAdapter<FavouriteInfor>(
        context,
        R.layout.rv_item_favourite,
        list
    ) {
    override fun bindViewHolder(holder: MyViewHolder, data: FavouriteInfor, position: Int) {
        val title = holder.findViewById<TextView>(R.id.tvCartoonTitle)
        title.text = data.title
        holder.itemView.setOnClickListener {
            onclick(
                position
            )
        }
        holder.itemView.setOnLongClickListener {
            longOnclick(position)
            false
        }
        val imageView = holder.findViewById<ImageView>(R.id.imgCartoon)
        if (data.imgUrl != null && data.imgUrl.isNotEmpty()) {
            Glide.with(holder.context).asDrawable().load(data.imgUrl).skipMemoryCache(true)
                .into(imageView)
        }
    }

    private lateinit var onclick: (p: Int) -> Unit
    private lateinit var longOnclick: (p: Int) -> Unit
    fun setOnClick(onclick: (p: Int) -> Unit, longOnclick: (p: Int) -> Unit) {
        this.onclick = onclick
        this.longOnclick = longOnclick
    }
}
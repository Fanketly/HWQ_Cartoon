package com.example.favourite

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
import com.example.base.headers
import com.example.hwq_cartoon.R
import com.example.repository.model.HistoryInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/14
 * Time: 13:13
 */
class HistoryRvAdapter(val context: Context, val list: List<HistoryInfor>) :
    MyAdapter<HistoryInfor>(
        context,
        R.layout.rv_item_history, list
    ) {

    @SuppressLint("SetTextI18n")
    override fun bindViewHolder(holder: MyViewHolder, data: HistoryInfor, position: Int) {
        val btn = holder.findViewById<Button>(R.id.btnHistoryCarryOn)
        val tvTime = holder.findViewById<TextView>(R.id.tvHistoryTime)
        val tvTitle = holder.findViewById<TextView>(R.id.tvHistoryTitle)
        val tvMark = holder.findViewById<TextView>(R.id.tvHistoryMark)
        val img=holder.findViewById<ImageView>(R.id.imgHistory)
        btn.setOnClickListener {

        }
        if (data.imgUrl != null) {
            Glide.with(context).asDrawable().load(GlideUrl(data.imgUrl, headers))
                .skipMemoryCache(true).into(img)
        }
        tvTitle.text = data.title
        tvTime.text=data.time
        tvMark.text="上次看至${data.mark+1}集"

    }


}
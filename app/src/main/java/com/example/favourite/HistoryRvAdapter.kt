package com.example.favourite

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
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

    override fun bindViewHolder(holder: MyViewHolder, data: HistoryInfor, position: Int) {
        val btn = holder.findViewById<Button>(R.id.btnHistoryCarryOn)
        val tvTime = holder.findViewById<TextView>(R.id.tvHistoryTime)
        val tvTitle = holder.findViewById<TextView>(R.id.tvHistoryTitle)
        val tvMark = holder.findViewById<TextView>(R.id.tvHistoryMark)
        tvTitle.text = data.title
    }

}
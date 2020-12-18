package com.example.detailed

import android.content.Context
import android.widget.TextView
import com.example.base.MyAdapter
import com.example.base.MyViewHolder
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/9/15
 * Time: 16:35
 */
class DetailRvAdapter(context: Context?, list: List<CartoonInfor>, var mark: Int)
    : MyAdapter<CartoonInfor>(context, R.layout.cartoon_dialog_rv_item, list) {
    lateinit var onclick: (Int) -> Unit
    override fun bindViewHolder(holder: MyViewHolder, data: CartoonInfor, position: Int) {
        val title = holder.findViewById<TextView>(R.id.tvCartoonDialogTitle)
        if (position == mark) {
            title.setBackgroundResource(R.drawable.text_boder_blue)
        } else {
            title.setBackgroundResource(R.drawable.textview_border)
        }
        title.text = data.titile
        holder.itemView.setOnClickListener { onclick.invoke(position) }
    }

    fun setOnClick(position: (Int) -> Unit) {
        this.onclick = position
    }
    fun itemChange(mark:Int){
        val mark2=this.mark
        this.mark=mark
        notifyItemChanged(mark2)
        notifyItemChanged(mark)
    }
}
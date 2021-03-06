package com.example.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.RvItemHistoryBinding
import com.example.repository.model.HistoryInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 11:02
 */
class HistoryRvAdapter(list: List<HistoryInfor>) :
    ViewBindingRvAdapter<HistoryInfor, RvItemHistoryBinding>(list) {
    @SuppressLint("SetTextI18n")
    override fun onBind(b: RvItemHistoryBinding, d: HistoryInfor, p: Int) {
        b.btnHistoryCarryOn.setOnClickListener { onclick(p) }
        b.tvHistoryTitle.text = d.title
        b.tvHistoryTime.text = "上次观看时间：${d.time}"
        b.tvHistoryMark.text = "上次看至${d.mark + 1}集"
        if (d.imgUrl.isNullOrBlank()) return
        b.imgHistory.load(d.imgUrl)
//        Glide.with(b.imgHistory).asDrawable().load(d.imgUrl)
//            .skipMemoryCache(true).into(b.imgHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<RvItemHistoryBinding> {
        return viewBinding(parent)
    }

}
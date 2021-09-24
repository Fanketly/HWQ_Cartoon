package com.example.adapter

import android.animation.ValueAnimator
import android.view.ViewGroup
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.databinding.RvItemCartoonShimmerBinding
import com.facebook.shimmer.Shimmer

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 20:18
 */
class HomeShimmerRvAdapter(list: List<Int>) :
    ViewBindingRvAdapter<Int, RvItemCartoonShimmerBinding>(list) {
    private var shimmer: Shimmer =
        Shimmer.AlphaHighlightBuilder().setDuration(5000L).setRepeatMode(ValueAnimator.RESTART)
            .build()

    override fun onBind(b: RvItemCartoonShimmerBinding, d: Int, p: Int) {
        b.layoutCartoon.setShimmer(shimmer)
//        b.tvCartoonTitle.text = d.title
//        b.root.setOnClickListener {
//            onclick(
//                p
//            )
//        }
//        b.tvCartoonType.text = d.type
//        if (d.img.isEmpty()) {
//            return
//        }
//        Log.i("TAG", "CartoonViewModel_pager:${d.img} ")
//        b.imgCartoon.load(d.img){
//            setHeader("Referer","https://manhua.dmzj.com/update_1.shtml")
//        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH<RvItemCartoonShimmerBinding> {
        return viewBinding(parent)
    }


}
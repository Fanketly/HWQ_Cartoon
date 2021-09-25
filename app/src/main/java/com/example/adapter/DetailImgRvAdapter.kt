package com.example.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.RelativeLayout
import coil.ImageLoader
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.databinding.CartoonImgRvItemBinding

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 9:29
 */
class DetailImgRvAdapter(
    list: List<String>,
    private val imageLoader: ImageLoader,
    private val isVertical: Boolean = false
) :
    ViewBindingRvAdapter<String, CartoonImgRvItemBinding>(list) {
    private val layoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT
    )

    override fun onBind(b: CartoonImgRvItemBinding, d: String, p: Int) {
        if (isVertical) {
            b.imageView.adjustViewBounds = isVertical
        } else {
            b.imageView.layoutParams = layoutParams
        }
        b.imageView.load(d) {
            setHeader("Referer", "https://manhua.dmzj.com/update_1.shtml")
            listener(onError = { request, _ ->
                b.imageView.setOnClickListener {
                    Log.i(TAG, "reload ")
                    imageLoader.enqueue(request)
                }
            }, onSuccess = { _, _ ->
                b.imageView.setOnClickListener { onclick(p) }
            })
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH<CartoonImgRvItemBinding> {
        return viewBinding(parent)
    }

}
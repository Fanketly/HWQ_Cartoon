package com.example.adapter

import android.util.Log
import android.view.ViewGroup
import coil.ImageLoader
import coil.load
import com.example.base.ViewBindingRvAdapter
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.databinding.CartoonImgRvItem2Binding

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/15
 * Time: 9:29
 */
class DetailImgRvAdapter2(
    list: List<String>,
    private val imageLoader: ImageLoader,
) :
    ViewBindingRvAdapter<String, CartoonImgRvItem2Binding>(list) {

    override fun onBind(b: CartoonImgRvItem2Binding, d: String, p: Int) {
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
    ): VH<CartoonImgRvItem2Binding> {
        return viewBinding(parent)
    }

}
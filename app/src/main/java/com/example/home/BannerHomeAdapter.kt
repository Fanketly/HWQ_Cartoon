package com.example.home

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.youth.banner.adapter.BannerAdapter


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/19
 * Time: 16:43
 */
class BannerHomeAdapter(val list: List<CartoonInfor>, val context: Context) :
    BannerAdapter<CartoonInfor, BannerHomeAdapter.BannerViewHolder>(list) {
    class BannerViewHolder(val img: ImageView) : RecyclerView.ViewHolder(img)


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: CartoonInfor?,
        position: Int,
        size: Int
    ) {
//        Glide.with(context).asDrawable().load(GlideUrl(data!!.img, headers))
//            .skipMemoryCache(true).into(holder.img)
        holder.img.setImageResource(R.drawable.guangao)
    }
}
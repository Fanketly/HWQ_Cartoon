package com.example.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfo
import com.youth.banner.adapter.BannerAdapter


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/19
 * Time: 16:43
 */
class BannerHomeAdapter(val list: List<CartoonInfo>, val context: Context) :
    BannerAdapter<CartoonInfo, BannerHomeAdapter.BannerViewHolder>(list) {
    class BannerViewHolder(val img: ImageView) : RecyclerView.ViewHolder(img) {
//        lateinit var img: ImageView
    }


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
//        val linearLayout = LinearLayout(context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val img = ImageView(context)
        img.scaleType = ImageView.ScaleType.FIT_XY
        img.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        val vh = BannerViewHolder(linearLayout)
//        vh.img = ImageView(context)
//        vh.img.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        linearLayout.gravity = Gravity.CENTER
//        linearLayout.addView(vh.img)
        return BannerViewHolder(img)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: CartoonInfo?,
        position: Int,
        size: Int
    ) {
//        Glide.with(context).asDrawable().load(GlideUrl(data!!.img, headers))
//            .error(R.drawable.guangao)
//            .fitCenter()
//            .skipMemoryCache(true).into(holder.img)
        when (position) {
            0 -> holder.img.setImageResource(R.drawable.lzsm1)
            2 -> holder.img.setImageResource(R.drawable.lzsy2)
            else -> holder.img.setImageResource(R.drawable.guangao)
        }
    }
}
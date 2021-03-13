package com.example.ui.home

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
    class BannerViewHolder(val img: ImageView) : RecyclerView.ViewHolder(img) {
//        lateinit var img: ImageView
    }


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
//        val linearLayout = LinearLayout(context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val img = ImageView(context)
        img.scaleType=ImageView.ScaleType.CENTER_CROP
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
        data: CartoonInfor?,
        position: Int,
        size: Int
    ) {
//        Glide.with(context).asDrawable().load(GlideUrl(data!!.img, headers))
//            .error(R.drawable.guangao)
//            .fitCenter()
//            .skipMemoryCache(true).into(holder.img)
        holder.img.setImageResource(R.drawable.guangao)
    }
}
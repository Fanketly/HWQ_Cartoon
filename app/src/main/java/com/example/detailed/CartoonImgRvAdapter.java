package com.example.detailed;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.base.MyAdapter;
import com.example.base.MyViewHolder;
import com.example.hwq_cartoon.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/19
 * Time: 22:08
 */
public class CartoonImgRvAdapter extends MyAdapter<byte[]> {
    private final Context context;
    private OnClick onClick;

    public CartoonImgRvAdapter(Context context, List<byte[]> list) {
        super(context, R.layout.cartoon_img_rv_item, list);
        this.context = context;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public void bindViewHolder(MyViewHolder holder, byte[] data, int position) {
        PhotoView imageView = holder.findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> onClick.onClick(position));
        Glide.with(context).asDrawable().load(data).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
//        Glide.with(context).asDrawable().load(data).skipMemoryCache(true).into(imageView);
//        imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    public interface OnClick {
        void onClick(int position);
    }
}

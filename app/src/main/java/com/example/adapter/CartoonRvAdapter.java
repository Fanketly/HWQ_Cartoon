package com.example.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.base.MyAdapter;
import com.example.base.MyViewHolder;
import com.example.hwq_cartoon.R;
import com.example.repository.model.CartoonInfor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/18
 * Time: 18:25
 */
public class CartoonRvAdapter extends MyAdapter<CartoonInfor> {
    private OnClick onClick;
    private final Context context;
    private final Headers headers;

    public CartoonRvAdapter(@NotNull List<CartoonInfor> list,int lay, Context context) {
        super(context, lay,list);
        this.context = context;
        headers = () -> {
            Map<String, String> map = new HashMap<>();
            map.put("Referer", "https://manhua.dmzj.com/update_1.shtml");
            return map;
        };
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }


    @Override
    public void bindViewHolder(@NotNull MyViewHolder holder, CartoonInfor data, int position) {
        TextView title = holder.findViewById(R.id.tvCartoonTitle);
        ConstraintLayout constraintLayout = holder.findViewById(R.id.layoutCartoon);
        title.setText(data.getTitile());
        constraintLayout.setOnClickListener(v -> {
            onClick.onClick(position);
        });
        TextView type=holder.findViewById(R.id.tvCartoonType);
       type.setText(data.getType());
//        constraintLayout.setOnLongClickListener(v -> {
//            onClick.longOnClick(position);
//            return false;
//        });
        ImageView imageView = holder.findViewById(R.id.imgCartoon);
            if (data.getImg() != null) {
//                Glide.with(context).asDrawable().load(glideUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                Glide.with(context).asDrawable().load(new GlideUrl(data.getImg(), headers)).skipMemoryCache(true).into(imageView);
            }
    }



    public interface OnClick {
        void onClick(int position);
//        void longOnClick(int position);
    }
}

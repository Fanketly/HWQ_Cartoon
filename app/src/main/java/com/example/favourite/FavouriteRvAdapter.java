package com.example.favourite;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.base.MyAdapter;
import com.example.base.MyViewHolder;
import com.example.hwq_cartoon.R;
import com.example.repository.model.FavouriteInfor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/31
 * Time: 14:57
 */
public class FavouriteRvAdapter extends MyAdapter<FavouriteInfor> {

    private OnClick onClick;
    private final Context context;
    private final Headers headers;

    public FavouriteRvAdapter(@NotNull List<FavouriteInfor> list, int layoutId, Context context) {
        super(context, layoutId,list);
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
    public void bindViewHolder(@NotNull MyViewHolder holder, FavouriteInfor data, int position) {
        TextView title = holder.findViewById(R.id.tvCartoonTitle);
//        ConstraintLayout constraintLayout = holder.findViewById(R.id.layoutCartoon);
        title.setText(data.getTitle());
        holder.getItemView().setOnClickListener(v -> onClick.onClick(position));
        holder.getItemView().setOnLongClickListener(v -> {
            onClick.longOnClick(position);
            return false;
        });
        ImageView imageView = holder.findViewById(R.id.imgCartoon);
        if (data.getImgUrl() != null) {
//            Glide.with(context).asDrawable().load(glideUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            Glide.with(context).asDrawable().load(new GlideUrl(data.getImgUrl(), headers)).skipMemoryCache(true).into(imageView);
        }
    }

    public interface OnClick {
        void onClick(int position);

        void longOnClick(int position);
    }


}

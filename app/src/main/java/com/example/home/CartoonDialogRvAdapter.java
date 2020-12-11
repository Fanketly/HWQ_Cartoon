package com.example.home;

import android.content.Context;
import android.widget.TextView;

import com.example.base.MyAdapter;
import com.example.base.MyViewHolder;
import com.example.hwq_cartoon.R;
import com.example.repository.model.CartoonInfor;

import java.util.List;


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/22
 * Time: 21:28
 */
public class CartoonDialogRvAdapter extends MyAdapter<CartoonInfor> {
    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public CartoonDialogRvAdapter(Context context, int layoutId, List<CartoonInfor> list) {
        super(context, layoutId,list);
    }

    @Override
    public void bindViewHolder(MyViewHolder holder, CartoonInfor data, int position) {
        TextView title = holder.findViewById(R.id.tvCartoonDialogTitle);
        title.setText(data.getTitile());
        holder.getItemView().setOnClickListener(v -> onClick.onClick(position));
    }
    public interface OnClick {
        void onClick(int position);
    }
}

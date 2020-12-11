package com.example.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/6/30
 * Time: 11:20
 */
public abstract class MyAdapter<T> extends RecyclerView.Adapter<MyViewHolder>  {
    private final List<T> list;
    private final Context context;
    private final int layoutId;

    public MyAdapter(Context context, int layoutId,List<T> list) {
        this.list=list;
        this.context = context;
        this.layoutId = layoutId;
    }





//    public abstract void delete(T t, int position);

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        bindViewHolder(holder, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public abstract void bindViewHolder(MyViewHolder holder, T data, int position);

}

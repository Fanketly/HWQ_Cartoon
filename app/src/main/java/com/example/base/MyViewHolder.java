package com.example.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/6/30
 * Time: 11:20
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
private final Context context;
private final View itemView;
private final SparseArray<View>sparseArray;
    public MyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.itemView=itemView;
        this.context=context;
        this.sparseArray=new SparseArray<>();
    }

    public View getItemView() {
        return itemView;
    }

    public Context getContext() {
        return context;
    }

    public <T extends View> T findViewById(int id){
        View view=sparseArray.get(id);
        if (view==null){
            view=itemView.findViewById(id);
            sparseArray.put(id,view);
        }
        return (T) view;
    }
}

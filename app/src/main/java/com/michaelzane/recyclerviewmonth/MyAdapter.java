package com.michaelzane.recyclerviewmonth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 类描述：适配器
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<DataBeans.DataBean> list;
    private Context context;
    private View view;
    private MyRecyclerViewInterface listener;

    public void setListener(MyRecyclerViewInterface listener) {
        this.listener = listener;
    }

    public MyAdapter(List<DataBeans.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getUserImg()).into(holder.userImg);
        holder.userName.setText(list.get(position).getUserName());
        holder.userAge.setText(list.get(position).getUserAge() + "岁");
        holder.userOccupation.setText(list.get(position).getOccupation());
        holder.userIntroduction.setText(list.get(position).getIntroduction());
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ExceptionActivity.class));
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImg;
        private TextView userName, userAge, userOccupation, userIntroduction;

        public ViewHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.userImg);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userAge = (TextView) itemView.findViewById(R.id.userAge);
            userOccupation = (TextView) itemView.findViewById(R.id.userOccupation);
            userIntroduction = (TextView) itemView.findViewById(R.id.userIntroduction);
        }
    }
}

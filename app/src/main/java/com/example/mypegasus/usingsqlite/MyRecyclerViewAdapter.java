package com.example.mypegasus.usingsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPegasus on 2015/10/20.
 */
class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<ContentValues> listData;
    private onItemLongClickListener mOnItemLongClickListener;

    public List<Integer> getHeights() {
        return heights;
    }

    private List<Integer> heights;

    public MyRecyclerViewAdapter(List<ContentValues> listData) {
        this.listData = listData;
        getRandomHeight(listData);
    }

    private void getRandomHeight(List<ContentValues> listData) {
        heights = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            heights.add((int) (100 + Math.random() * 200));
        }
    }

    interface onItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setItemLongClickListener(onItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_cell, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        System.out.println(params == null);
//        System.out.println(heights);
//        params.height = heights.get(position);
        params.height = (int) (100 + Math.random() * 200);
        holder.itemView.setLayoutParams(params);

        ContentValues cv = listData.get(position);
//        System.out.println(cv);
        holder.tvName.setText(cv.getAsString("name"));
        holder.tvSex.setText(cv.getAsString("sex"));

        if(mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvSex;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSex = (TextView) itemView.findViewById(R.id.tvSex);
        }

    }
}

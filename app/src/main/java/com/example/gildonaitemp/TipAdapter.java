package com.example.gildonaitemp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private List<String> tipList;

    public TipAdapter(List<String> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        holder.button.setText(tipList.get(position));

        holder.button.setBackgroundResource(R.drawable.tip_button);

        holder.button.setOnClickListener(v -> {
            if (holder.layout.getVisibility() == View.GONE) {
                // 이미지와 텍스트뷰 보이기
                holder.layout.setVisibility(View.VISIBLE);
                holder.button.setTextColor(Color.WHITE);
                holder.button.setBackgroundResource(R.drawable.tip_selected_button);

            } else {
                // 이미지와 텍스트뷰 숨기기
                holder.layout.setVisibility(View.GONE);
                holder.button.setTextColor(Color.BLACK);
                holder.button.setBackgroundResource(R.drawable.tip_button);
            }

            holder.itemView.requestLayout();
        });
    }

    public void updateList(List<String> newList) {
        tipList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        Button button;
        LinearLayout layout;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.tipButton);
            layout = itemView.findViewById(R.id.tipDetail);
        }
    }
}
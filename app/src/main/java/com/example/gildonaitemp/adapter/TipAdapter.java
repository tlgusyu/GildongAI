package com.example.gildonaitemp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.R;

import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private List<TipItem> tipList;

    public TipAdapter(List<TipItem> tipList) {
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
        TipItem item = tipList.get(position);
        holder.button.setText(item.getTitle());
        holder.tipText.setText(item.getDescription());

        if (item.hasImage()) {
            holder.tipImage.setVisibility(View.VISIBLE);
            holder.tipImage.setImageResource(item.getImageResId());
        } else {
            holder.tipImage.setVisibility(View.GONE);
        }
        holder.layout.setVisibility(View.GONE); // 초기 상태
        holder.button.setTextColor(Color.BLACK);
        holder.button.setBackgroundResource(R.drawable.tip_button);

        holder.button.setOnClickListener(v -> {
            boolean isVisible = holder.layout.getVisibility() == View.VISIBLE;

            if (isVisible) {
                holder.layout.setVisibility(View.GONE);
                holder.button.setTextColor(Color.BLACK);
                holder.button.setBackgroundResource(R.drawable.tip_button);
            } else {
                holder.layout.setVisibility(View.VISIBLE);
                holder.button.setTextColor(Color.WHITE);
                holder.button.setBackgroundResource(R.drawable.tip_selected_button);
            }

            holder.itemView.requestLayout();
        });
    }

    public void updateList(List<TipItem> newList) {
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
        ImageView tipImage;
        TextView tipText;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.tipButton);
            layout = itemView.findViewById(R.id.tipDetail);
            tipImage = itemView.findViewById(R.id.tipImage);
            tipText = itemView.findViewById(R.id.tipText);
        }
    }

}
package com.example.gildonaitemp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);

        holder.textViewCategory.setText(notification.getCategory());
        holder.textViewMessage.setText(notification.getMessage());

        switch (notification.getType()) {
            case "차량 점검":
                holder.imageViewIcon.setImageResource(R.drawable.ic_vehicle_check);
                break;
            case "안전":
                holder.imageViewIcon.setImageResource(R.drawable.ic_safety);
                break;
            case "차량 소모품":
                holder.imageViewIcon.setImageResource(R.drawable.ic_vehicle_parts);
                break;
            case "지난 알림":
                holder.imageViewIcon.setImageResource(R.drawable.ic_past_notification);
                break;
            default:
                holder.imageViewIcon.setImageResource(R.drawable.ic_vehicle_check);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewCategory;
        TextView textViewMessage;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.AlarmIcon); // 아이콘
            textViewCategory = itemView.findViewById(R.id.AlarmCategory); // 카테고리
            textViewMessage = itemView.findViewById(R.id.AlarmText); // 텍스트
        }
    }
}

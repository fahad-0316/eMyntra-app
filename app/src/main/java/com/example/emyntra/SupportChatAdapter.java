package com.example.emyntra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportChatAdapter extends RecyclerView.Adapter<SupportChatAdapter.SupportViewHolder> {

    private List<SupportMessage> messageList;
    private String currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public SupportChatAdapter(List<SupportMessage> messageList) {
        this.messageList = messageList;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            currentUserId = "";
        }
    }

    @Override
    public int getItemViewType(int position) {
        SupportMessage message = messageList.get(position);
        if (message.getSenderId() != null && message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support_user, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support_agent, parent, false);
        }
        return new SupportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportViewHolder holder, int position) {
        SupportMessage message = messageList.get(position);

        // Set Message Text
        holder.tvMessage.setText(message.getMessageText());

        // Set Timestamp
        if (message.getTimestamp() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String time = sdf.format(new Date(message.getTimestamp()));
            holder.tvTimestamp.setText(time);
        } else {
            holder.tvTimestamp.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SupportViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp;

        public SupportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp); // Bind the timestamp view
        }
    }
}
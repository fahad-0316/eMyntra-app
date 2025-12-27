package com.example.emyntra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatList;
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    // Determine if the message is from User or AI
    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).isUser()) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_AI;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // Inflate the correct layout based on the type
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_ai, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        // Set the text of the message
        holder.tvMessage.setText(chatList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // Simple ViewHolder to hold the TextView
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }
}
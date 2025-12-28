package com.example.emyntra;

import android.os.Bundle;
import android.os.Handler; // Import for the timer
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SupportChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ImageView btnBack;

    private SupportChatAdapter adapter;
    private List<SupportMessage> messageList;

    private FirebaseFirestore db;
    private CollectionReference chatRef;
    private String userId;

    // Flag to track if we have already sent the auto-reply in this session
    private boolean hasSentAutoReply = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_support_chat);

            db = FirebaseFirestore.getInstance();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Path: chats -> [UserID] -> messages
                chatRef = db.collection("chats").document(userId).collection("messages");
            } else {
                Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            recyclerView = findViewById(R.id.rv_support_chat);
            etMessage = findViewById(R.id.et_message);
            btnSend = findViewById(R.id.btn_send);
            btnBack = findViewById(R.id.btn_back);

            messageList = new ArrayList<>();
            adapter = new SupportChatAdapter(messageList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            listenForMessages();

            btnSend.setOnClickListener(v -> sendMessage());
            btnBack.setOnClickListener(v -> finish());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Crash prevented: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        SupportMessage message = new SupportMessage(text, userId);

        chatRef.add(message)
                .addOnSuccessListener(documentReference -> {
                    // Success: Clear input
                    etMessage.setText("");
                    // Scroll to bottom
                    recyclerView.scrollToPosition(messageList.size() - 1);

                    // --- SMART AUTO-REPLY LOGIC ---
                    // Only send auto-reply if we haven't done so in this session
                    if (!hasSentAutoReply) {
                        simulateAgentReply();
                        hasSentAutoReply = true; // Lock it so it doesn't happen again this session
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SupportChatActivity.this, "Failed to send: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Simulates a Customer Support Agent replying after 2 seconds.
     */
    private void simulateAgentReply() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // We use a sender ID "agent" which is DIFFERENT from your userId.
                // The Adapter logic checks: if (id == userId) -> Right Side. Else -> Left Side.
                SupportMessage reply = new SupportMessage(
                        "Thank you for contacting eMyntra Support! An agent will review your query shortly.",
                        "agent"
                );

                // Add to database (This triggers the listener automatically)
                chatRef.add(reply);
            }
        }, 2000); // 2000ms = 2 seconds delay
    }

    private void listenForMessages() {
        if (chatRef == null) return;

        chatRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        if (value != null) {
                            messageList.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                try {
                                    SupportMessage message = doc.toObject(SupportMessage.class);
                                    messageList.add(message);
                                } catch (Exception e) {
                                    // Skip invalid message
                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (!messageList.isEmpty()) {
                                recyclerView.scrollToPosition(messageList.size() - 1);
                            }
                        }
                    }
                });
    }
}
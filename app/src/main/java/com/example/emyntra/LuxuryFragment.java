package com.example.emyntra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LuxuryFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private Spinner spinnerModel;
    private ChatAdapter adapter;
    private List<ChatMessage> chatList;
    private OkHttpClient client;

    // --- API KEYS ---
    private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY";
    private static final String PERPLEXITY_API_KEY = "YOUR_PERPLEXITY_API_KEY";

    // --- ENDPOINTS ---
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;
    private static final String PERPLEXITY_URL = "https://api.perplexity.ai/chat/completions";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the chat layout for this fragment
        return inflater.inflate(R.layout.fragment_luxury, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        recyclerView = view.findViewById(R.id.rv_chat);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        spinnerModel = view.findViewById(R.id.spinner_model);

        // Setup Spinner Options
        String[] modelOptions = {"Google Gemini", "Perplexity AI"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, modelOptions);
        spinnerModel.setAdapter(spinnerAdapter);

        // Setup Chat List
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Setup Network Client
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        addResponse("Hello! I am your AI assistant. Choose a model above and ask me anything about fashion trends or products!");

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add User Message to UI
                chatList.add(new ChatMessage(message, true));
                adapter.notifyItemInserted(chatList.size() - 1);
                recyclerView.scrollToPosition(chatList.size() - 1);
                etMessage.setText("");

                // Check which model is selected
                String selectedModel = spinnerModel.getSelectedItem().toString();
                if (selectedModel.equals("Google Gemini")) {
                    sendToGemini(message);
                } else {
                    sendToPerplexity(message);
                }
            }
        });
    }

    // --- GEMINI LOGIC ---
    private void sendToGemini(String message) {
        String jsonBody = "{"
                + "\"contents\": [{"
                + "\"parts\": [{\"text\": \"You are a shopping assistant for eMyntra. Answer concisely: " + message + "\"}]"
                + "}]"
                + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(GEMINI_URL).post(body).build();

        performNetworkRequest(request, "Gemini");
    }

    // --- PERPLEXITY LOGIC ---
    private void sendToPerplexity(String message) {
        // Perplexity uses OpenAI-compatible format
        String jsonBody = "{"
                + "\"model\": \"sonar\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"You are a helpful shopping assistant for eMyntra.\"},"
                + "  {\"role\": \"user\", \"content\": \"" + message + "\"}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(PERPLEXITY_URL)
                .addHeader("Authorization", "Bearer " + PERPLEXITY_API_KEY)
                .post(body)
                .build();

        performNetworkRequest(request, "Perplexity");
    }

    // --- SHARED NETWORK HANDLER ---
    private void performNetworkRequest(Request request, String modelType) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> addResponse("Failed to connect to " + modelType + ". Check internet."));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        String aiText = "";

                        JSONObject jsonObject = new JSONObject(responseData);

                        if (modelType.equals("Gemini")) {
                            aiText = jsonObject.getJSONArray("candidates")
                                    .getJSONObject(0).getJSONObject("content")
                                    .getJSONArray("parts").getJSONObject(0).getString("text");
                        } else {
                            aiText = jsonObject.getJSONArray("choices")
                                    .getJSONObject(0).getJSONObject("message").getString("content");
                        }

                        final String finalText = aiText;
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> addResponse(finalText));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> addResponse("Error parsing response from " + modelType));
                        }
                    }
                } else {
                    final String errorMsg = response.body() != null ? response.body().string() : "Unknown Error";
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> addResponse(modelType + " Error: " + response.code()));
                    }
                }
            }
        });
    }

    private void addResponse(String response) {
        chatList.add(new ChatMessage(response, false));
        adapter.notifyItemInserted(chatList.size() - 1);
        recyclerView.scrollToPosition(chatList.size() - 1);
    }
}
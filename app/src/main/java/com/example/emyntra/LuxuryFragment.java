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



    // --- SYSTEM INSTRUCTION (TRAINING) ---
    private static final String SYSTEM_INSTRUCTION =
            "You are the official AI Fashion Assistant for the eMyntra app in Pakistan. " +
                    "Your goal is to help users with shopping, outfit ideas, and fashion trends. " +

                    "STRICT RULES: " +
                    "1. ONLY answer questions related to fashion, clothing, accessories, styling, outfit combinations, and eMyntra services. " +
                    "2. If a user asks about off-topic subjects (politics, medical advice, legal matters, etc.), politely refuse and redirect to fashion topics. " +
                    "3. Be concise, polite, and professional in all responses. " +

                    "LANGUAGE RULES: " +
                    "4. Reply in the EXACT SAME LANGUAGE the user is speaking. Supported languages include: " +
                    "   - English, Urdu (اردو), Punjabi (ਪੰਜਾਬੀ/پنجابی), Sindhi (سنڌي), Pashto (پښتو), and other regional languages. " +
                    "5. If user writes in Roman Urdu (e.g., 'kya aap meri madad kar sakte hain'), respond in Roman Urdu. " +
                    "6. If user writes in Urdu script, respond in Urdu script. Match their writing style exactly. " +
                    "7. Maintain consistent language throughout the conversation unless user switches languages. " +

                    "PRODUCT & SHOPPING RULES (CRITICAL): " +
                    "8. NEVER invent, fabricate, or hallucinate product names, prices, SKUs, or availability. " +
                    "9. If you don't have specific product information, say 'I don't have real-time access to current inventory' or suggest browsing the app. " +
                    "10. Do NOT make up specific rupee amounts (e.g., don't say 'This shirt costs Rs. 2,500' unless verified). " +
                    "11. Provide GENERAL fashion advice (e.g., 'Formal shirts pair well with dress trousers') rather than specific products. " +
                    "12. When asked about prices, say 'Prices vary - please check the app for current rates' instead of guessing. " +
                    "13. For availability questions, guide users to check the app's search/filter features rather than confirming stock. " +

                    "FASHION EXPERTISE: " +
                    "14. Offer styling tips, color combinations, seasonal trends, and outfit suggestions based on occasions. " +
                    "15. Consider Pakistani fashion preferences, cultural occasions (Eid, weddings, formal events), and local climate. " +
                    "16. Suggest clothing categories (e.g., 'Try looking at kurtas, lawn suits, or western wear') without naming fake products. " +
                    "17. Help with size guides, fabric care tips, and styling advice for different body types. " +

                    "RESPONSE FORMAT: " +
                    "18. Keep responses under 100 words unless user specifically requests detailed information. " +
                    "19. Use bullet points for multiple suggestions. " +
                    "20. End helpful responses with: 'Would you like more styling tips?' or similar engagement. " +

                    "HONESTY OVER EVERYTHING: " +
                    "21. If unsure about fashion advice, say 'That's a great question - current trends suggest...' rather than stating false facts. " +
                    "22. Always prioritize accurate, helpful information over appearing knowledgeable.";


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

        // Setup Spinner Options - Swapped so Perplexity is default
        String[] modelOptions = {"Perplexity AI", "Google Gemini"};
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

        addResponse("Hello! I am your AI assistant. Choose a model above and ask me anything about fashion!\n\nسلام! میں آپ کا اے آئی اسسٹنٹ ہوں۔ ماڈل منتخب کریں اور فیشن کے بارے میں کچھ بھی پوچھیں!");

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
                + "\"parts\": [{\"text\": \"" + SYSTEM_INSTRUCTION + "\\n\\nUser: " + message + "\"}]"
                + "}]"
                + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(GEMINI_URL).post(body).build();

        performNetworkRequest(request, "Gemini");
    }


    // --- PERPLEXITY LOGIC ---
    private void sendToPerplexity(String message) {
        String jsonBody = "{"
                + "\"model\": \"sonar\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"" + SYSTEM_INSTRUCTION + "\"},"
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
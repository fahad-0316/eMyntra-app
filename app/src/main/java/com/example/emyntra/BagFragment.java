package com.example.emyntra;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BagFragment extends Fragment {

    private ImageView ivPreview;
    private Button btnPick, btnIdentify;
    private TextView tvResult;
    private ProgressBar progressBar;
    private Bitmap selectedBitmap;

    private static final int PICK_IMAGE_REQUEST = 1;

    // Hugging Face API Key



    private static final String HF_API_TOKEN = "YOUR_HUGGING_FACE_TOKEN";

    // Using Hugging Face Vision Transformer model for image classification
    private static final String API_URL = "https://router.huggingface.co/hf-inference/models/google/vit-base-patch16-224";



    private OkHttpClient client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ensure Bottom Navigation is visible for this tab
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
        }

        ivPreview = view.findViewById(R.id.iv_preview);
        btnPick = view.findViewById(R.id.btn_pick_image);
        btnIdentify = view.findViewById(R.id.btn_identify);
        tvResult = view.findViewById(R.id.tv_result);
        progressBar = view.findViewById(R.id.progress_bar);

        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        btnPick.setOnClickListener(v -> openGallery());

        btnIdentify.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                identifyImage(selectedBitmap);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                if (getActivity() != null) {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    selectedBitmap = BitmapFactory.decodeStream(inputStream);
                    ivPreview.setImageBitmap(selectedBitmap);

                    // Enable button now that we have an image
                    btnIdentify.setEnabled(true);
                    btnIdentify.setAlpha(1.0f);
                    tvResult.setText("Image selected. Click 'Identify' to analyze.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void identifyImage(Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        tvResult.setText("Analyzing with Hugging Face...");
        btnIdentify.setEnabled(false);

        // Convert Bitmap to Byte Array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        // Build Request for Hugging Face
        RequestBody requestBody = RequestBody.create(byteArray, MediaType.parse("application/octet-stream"));
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + HF_API_TOKEN)
                .post(requestBody)
                .build();

        // Send to Hugging Face
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnIdentify.setEnabled(true);
                        tvResult.setText("Connection Failed: " + e.getMessage());
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnIdentify.setEnabled(true);

                        if (response.isSuccessful()) {
                            try {
                                // Parse Hugging Face JSON Response
                                JSONArray jsonArray = new JSONArray(responseData);
                                if (jsonArray.length() > 0) {
                                    JSONObject topResult = jsonArray.getJSONObject(0);
                                    String label = topResult.getString("label");
                                    String score = String.format("%.1f%%", topResult.getDouble("score") * 100);

                                    tvResult.setText("I think this is a: \n" + label.toUpperCase() + "\n(Confidence: " + score + ")");
                                } else {
                                    tvResult.setText("Could not identify the item.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvResult.setText("Error parsing result.");
                            }
                        } else {
                            tvResult.setText("API Error: " + response.code());
                        }
                    });
                }
            }
        });
    }
}
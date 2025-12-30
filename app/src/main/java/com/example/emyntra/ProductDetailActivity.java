package com.example.emyntra;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvBrand, tvPrice;
    private Button btnAddToBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Add back button to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }

        // Initialize Views
        ivImage = findViewById(R.id.iv_product_detail_image);
        tvBrand = findViewById(R.id.tv_product_detail_brand);
        tvPrice = findViewById(R.id.tv_product_detail_price);
        btnAddToBag = findViewById(R.id.btn_add_to_bag);

        // Get Data from Intent
        String brand = getIntent().getStringExtra("BRAND");
        String price = getIntent().getStringExtra("PRICE");
        String imageName = getIntent().getStringExtra("IMAGE_NAME");

        // Set Data
        if (brand != null) {
            tvBrand.setText(brand);
        }
        if (price != null) {
            tvPrice.setText(price);
        }

        // Load Image dynamically
        if (imageName != null) {
            int imageId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageId != 0) {
                ivImage.setImageResource(imageId);
            } else {
                ivImage.setImageResource(R.drawable.ic_placeholder_image);
            }
        } else {
            ivImage.setImageResource(R.drawable.ic_placeholder_image);
        }

        // Add to Bag Click
        if (btnAddToBag != null) {
            btnAddToBag.setOnClickListener(v ->
                    Toast.makeText(ProductDetailActivity.this, "Added to Bag", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
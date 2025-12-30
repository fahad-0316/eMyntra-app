package com.example.emyntra;

import android.content.Context;
import android.content.Intent; // Import Intent
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductItem> productList;

    public ProductAdapter(Context context, List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the grid_item_product layout
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem item = productList.get(position);

        // Bind Data
        holder.tvBrand.setText(item.getBrand());
        holder.tvPrice.setText(item.getPrice());

        // --- Handle Image Loading ---
        // Dynamically find the drawable resource ID based on the imageName string
        int imageId = context.getResources().getIdentifier(item.getImageName(), "drawable", context.getPackageName());

        if (imageId != 0) {
            holder.ivProductImage.setImageResource(imageId);
        } else {
            // Fallback if image is not found
            holder.ivProductImage.setImageResource(R.drawable.ic_placeholder_image);
        }

        // --- Handle Like Button State ---
        // Clear any tint so we can see the red/outline images clearly
        holder.ivWishlist.setColorFilter(null);

        if (item.isLiked()) {
            holder.ivWishlist.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.ivWishlist.setImageResource(R.drawable.ic_heart_outline);
        }

        // Like Button Click Listener
        holder.ivWishlist.setOnClickListener(v -> {
            boolean newState = !item.isLiked();
            item.setLiked(newState);

            // Update UI immediately
            holder.ivWishlist.setColorFilter(null);
            if (newState) {
                holder.ivWishlist.setImageResource(R.drawable.ic_heart_filled);
            } else {
                holder.ivWishlist.setImageResource(R.drawable.ic_heart_outline);
            }
        });

        // --- NEW: ITEM CLICK LISTENER (Opens Detail Page) ---
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            // Pass data to the detail activity
            intent.putExtra("BRAND", item.getBrand());
            intent.putExtra("PRICE", item.getPrice());
            intent.putExtra("IMAGE_NAME", item.getImageName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivWishlist;
        TextView tvBrand, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            ivWishlist = itemView.findViewById(R.id.iv_wishlist);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
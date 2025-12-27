package com.example.emyntra;

import android.content.Context;
import android.graphics.Color;
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

        // --- Handle Like Button Initial State ---
        updateLikeButton(holder.ivWishlist, item.isLiked());

        // --- Handle Like Button Click ---
        holder.ivWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Toggle the boolean state in the data model
                boolean newState = !item.isLiked();
                item.setLiked(newState);

                // 2. Update the UI immediately to reflect the change
                updateLikeButton(holder.ivWishlist, newState);
            }
        });
    }

    /**
     * Helper method to update the heart icon based on the liked state.
     */
    private void updateLikeButton(ImageView imageView, boolean isLiked) {
        // Clear any previous color filter so the icons show their true colors
        imageView.setColorFilter(null);

        if (isLiked) {
            // User liked the item: Show red filled heart
            imageView.setImageResource(R.drawable.ic_heart_filled);
        } else {
            // Item not liked: Show outline heart
            imageView.setImageResource(R.drawable.ic_heart_outline);
        }
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
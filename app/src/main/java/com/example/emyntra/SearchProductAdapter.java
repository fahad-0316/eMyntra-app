package com.example.emyntra;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchViewHolder> {

    private Context context;
    private List<ProductItem> productList;

    public SearchProductAdapter(Context context, List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_search_product layout which has the cart icon
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_product, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        ProductItem item = productList.get(position);

        // Bind Data
        holder.tvBrand.setText(item.getBrand());
        holder.tvPrice.setText(item.getPrice());

        // --- Handle Image Loading ---
        // Added null check for imageName
        if (item.getImageName() != null) {
            int imageId = context.getResources().getIdentifier(item.getImageName(), "drawable", context.getPackageName());
            if (imageId != 0) {
                holder.ivProductImage.setImageResource(imageId);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.ic_placeholder_image);
            }
        } else {
             holder.ivProductImage.setImageResource(R.drawable.ic_placeholder_image);
        }

        // --- Handle Cart Button Initial State ---
        updateCartButton(holder.ivAddToCart, item.isAddedToCart());

        // --- Handle Cart Button Click ---
        holder.ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Toggle the state
                boolean newState = !item.isAddedToCart();
                item.setAddedToCart(newState);

                // 2. Update UI
                updateCartButton(holder.ivAddToCart, newState);

                // 3. Show Feedback
                if(newState) {
                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- NEW: ITEM CLICK LISTENER (Opens Detail Page) ---
        // Added this listener so Search items also open the detail page
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            // Pass data to the detail activity
            intent.putExtra("BRAND", item.getBrand());
            intent.putExtra("PRICE", item.getPrice());
            intent.putExtra("IMAGE_NAME", item.getImageName());
            context.startActivity(intent);
        });
    }

    /**
     * Helper method to update the cart icon based on state.
     */
    private void updateCartButton(ImageView imageView, boolean isAdded) {
        // Clear filters to show true colors (Grey Outline vs Pink Fill)
        imageView.setColorFilter(null);

        if (isAdded) {
            imageView.setImageResource(R.drawable.ic_cart_filled);
        } else {
            imageView.setImageResource(R.drawable.ic_cart_outline);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivAddToCart;
        TextView tvBrand, tvPrice;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            ivAddToCart = itemView.findViewById(R.id.iv_add_to_cart); // Helper for Cart Icon
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
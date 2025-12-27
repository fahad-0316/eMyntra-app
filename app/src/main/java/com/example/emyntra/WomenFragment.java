package com.example.emyntra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WomenFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductItem> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_women, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_women);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadProductData();

        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }

    private void loadProductData() {
        // Create 20 items for "Women" with proper names and prices in Rs.
        productList = new ArrayList<>();
        productList.add(new ProductItem("Floral Print Kurti", "Rs. 2,890", "women_1"));
        productList.add(new ProductItem("Lawn Kurta Set", "Rs. 4,990", "women_2"));
        productList.add(new ProductItem("Embroidered Suit", "Rs. 7,990", "women_3"));
        productList.add(new ProductItem("Solid Lawn Kurta", "Rs. 2,490", "women_4"));
        productList.add(new ProductItem("Casual Printed Top", "Rs. 1,290", "women_5"));
        productList.add(new ProductItem("Ethnic Khussa", "Rs. 1,990", "women_6"));
        productList.add(new ProductItem("Printed Lawn Saree", "Rs. 5,500", "women_7"));
        productList.add(new ProductItem("Block Heeled Sandals", "Rs. 3,200", "women_8"));
        productList.add(new ProductItem("Skinny Fit Jeans", "Rs. 3,990", "women_9"));
        productList.add(new ProductItem("Liquid Foundation", "Rs. 2,150", "women_10"));
        productList.add(new ProductItem("Women's Blazer", "Rs. 6,990", "women_11"));
        productList.add(new ProductItem("Solid Casual Top", "Rs. 1,790", "women_12"));
        productList.add(new ProductItem("Crossbody Handbag", "Rs. 4,200", "women_13"));
        productList.add(new ProductItem("Women's Sneakers", "Rs. 5,490", "women_14"));
        productList.add(new ProductItem("Chikankari Kurti", "Rs. 3,190", "women_15"));
        productList.add(new ProductItem("Hyaluronic Serum", "Rs. 2,800", "women_16"));
        productList.add(new ProductItem("High-Waist Jeggings", "Rs. 2,990", "women_17"));
        productList.add(new ProductItem("Walking Shoes", "Rs. 6,200", "women_18"));
        productList.add(new ProductItem("Women's Wallet", "Rs. 1,990", "women_19"));
        productList.add(new ProductItem("Rose Gold Watch", "Rs. 9,500", "women_20"));



        productList.add(new ProductItem("Floral Print Kurti", "Rs. 2,890", "all_2"));
        productList.add(new ProductItem("Lawn Kurta Set", "Rs. 4,990", "all_4"));
        productList.add(new ProductItem("Embroidered Suit", "Rs. 7,990", "all_6"));
        productList.add(new ProductItem("Solid Lawn Kurta", "Rs. 2,490", "all_8"));
        productList.add(new ProductItem("Block Heeled Sandals", "Rs. 3,200", "all_10"));
        productList.add(new ProductItem("Skinny Fit Jeans", "Rs. 3,990", "all_12"));
        productList.add(new ProductItem("Ethnic Khussa", "Rs. 1,990", "all_14"));
        productList.add(new ProductItem("Printed Lawn Saree", "Rs. 5,500", "all_16"));
        productList.add(new ProductItem("Crossbody Handbag", "Rs. 4,200", "all_18"));
        productList.add(new ProductItem("Rose Gold Watch", "Rs. 9,500", "all_20"));
    }
}
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

public class MenFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductItem> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_men, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_men);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadProductData();

        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }

    private void loadProductData() {
        // Create 20 items for "Men" with proper names and prices in Rs.
        productList = new ArrayList<>();
        productList.add(new ProductItem("Casual Check Shirt", "Rs. 2,190", "men_1"));
        productList.add(new ProductItem("Graphic T-Shirt", "Rs. 1,290", "men_2"));
        productList.add(new ProductItem("Men's Sneakers", "Rs. 7,490", "men_3"));
        productList.add(new ProductItem("Slim Fit Jeans", "Rs. 3,200", "men_4"));
        productList.add(new ProductItem("Printed Relaxed Fit", "Rs. 1,850", "men_5"));
        productList.add(new ProductItem("Formal Cotton Shirt", "Rs. 2,500", "men_6"));
        productList.add(new ProductItem("Active T-Shirt", "Rs. 1,190", "men_7"));
        productList.add(new ProductItem("Analog Watch", "Rs. 15,500", "men_8"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 2,200", "men_9"));
        productList.add(new ProductItem("Leather Boots", "Rs. 6,500", "men_10"));
        productList.add(new ProductItem("Striped Shirt", "Rs. 2,390", "men_11"));
        productList.add(new ProductItem("Running Shoes", "Rs. 4,990", "men_12"));
        productList.add(new ProductItem("Leather Wallet", "Rs. 1,800", "men_13"));
        productList.add(new ProductItem("Printed T-Shirt", "Rs. 1,450", "men_14"));
        productList.add(new ProductItem("Formal Trousers", "Rs. 3,100", "men_15"));
        productList.add(new ProductItem("Men's Kurta", "Rs. 2,800", "men_16"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 1,790", "men_17"));
        productList.add(new ProductItem("Digital Watch", "Rs. 12,500", "men_18"));
        productList.add(new ProductItem("Cargo Trousers", "Rs. 3,400", "men_19"));
        productList.add(new ProductItem("Straight Fit Jeans", "Rs. 2,990", "men_20"));


        productList.add(new ProductItem("Casual Check Shirt", "Rs. 2,190", "all_1"));
        productList.add(new ProductItem("Men's Sneakers", "Rs. 7,490", "all_3"));
        productList.add(new ProductItem("Slim Fit Jeans", "Rs. 3,200", "all_7"));
        productList.add(new ProductItem("Analog Watch", "Rs. 15,500", "all_13"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 2,200", "all_15"));
        productList.add(new ProductItem("Leather Boots", "Rs. 6,500", "all_17"));

        productList.add(new ProductItem("Digital Watch", "Rs. 12,500", "all_19"));

    }
}
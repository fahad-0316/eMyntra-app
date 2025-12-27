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

public class KidsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductItem> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kids, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_kids);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadProductData();

        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }

    private void loadProductData() {
        // Create 20 items for "Kids" with proper names and prices in Rs.
        productList = new ArrayList<>();
        productList.add(new ProductItem("Printed T-Shirt", "Rs. 990", "kids_1"));
        productList.add(new ProductItem("Denim Jeans", "Rs. 1,490", "kids_2"));
        productList.add(new ProductItem("Graphic Hoodie", "Rs. 1,990", "kids_3"));
        productList.add(new ProductItem("Kids Clogs", "Rs. 6,490", "kids_4"));
        productList.add(new ProductItem("Baby Romper Set", "Rs. 3,500", "kids_5"));
        productList.add(new ProductItem("Fashion Doll", "Rs. 2,800", "kids_6"));
        productList.add(new ProductItem("Girls Kurta", "Rs. 2,490", "kids_7"));
        productList.add(new ProductItem("Boys Kurta", "Rs. 2,200", "kids_8"));
        productList.add(new ProductItem("Toy Car Set", "Rs. 1,490", "kids_9"));
        productList.add(new ProductItem("Kids Polo Shirt", "Rs. 2,790", "kids_10"));
        productList.add(new ProductItem("Kids Sneakers", "Rs. 5,990", "kids_11"));
        productList.add(new ProductItem("Building Blocks", "Rs. 4,500", "kids_12"));
        productList.add(new ProductItem("Full Sleeve Shirt", "Rs. 1,350", "kids_13"));
        productList.add(new ProductItem("Girls Sandals", "Rs. 1,700", "kids_14"));
        productList.add(new ProductItem("Boys Shalwar Kameez", "Rs. 3,200", "kids_15"));
        productList.add(new ProductItem("Baby Stroller", "Rs. 7,500", "kids_16"));
        productList.add(new ProductItem("Cartoon Backpack", "Rs. 2,990", "kids_17"));
        productList.add(new ProductItem("Girls Lehenga Choli", "Rs. 4,200", "kids_18"));
        productList.add(new ProductItem("Superhero T-Shirt", "Rs. 1,290", "kids_19"));
        productList.add(new ProductItem("Sport Shoes", "Rs. 3,190", "kids_20"));

        productList.add(new ProductItem("Graphic T-Shirt", "Rs. 1,290", "all_5"));
        productList.add(new ProductItem("Printed Relaxed Fit", "Rs. 1,850", "all_9"));
        productList.add(new ProductItem("Active T-Shirt", "Rs. 1,190", "all_11"));
    }
}
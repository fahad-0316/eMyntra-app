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

public class AllFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductItem> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment, which is now fragment_all.xml
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Find the RecyclerView
        recyclerView = view.findViewById(R.id.rv_all);

        // 2. Set the layout manager (2 columns)
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // 3. Load your product data
        loadProductData();

        // 4. Create and set the adapter
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }

    private void loadProductData() {
        // Create 20 items for "All" (a mix of Men and Women) with prices in Rs.
        productList = new ArrayList<>();

        productList.add(new ProductItem("Women's Blazer", "Rs. 6,990", "women_11"));
        productList.add(new ProductItem("Casual Check Shirt", "Rs. 2,190", "all_1"));
        productList.add(new ProductItem("Baby Romper Set", "Rs. 3,500", "kids_5"));
        productList.add(new ProductItem("Men's Sneakers", "Rs. 7,490", "men_3"));
        productList.add(new ProductItem("Crossbody Handbag", "Rs. 4,200", "all_18"));
        productList.add(new ProductItem("Floral Print Kurti", "Rs. 2,890", "women_1"));
        productList.add(new ProductItem("Sport Shoes", "Rs. 3,190", "kids_20"));
        productList.add(new ProductItem("Formal Trousers", "Rs. 3,100", "men_15"));
        productList.add(new ProductItem("Printed Relaxed Fit", "Rs. 1,850", "all_9"));
        productList.add(new ProductItem("Rose Gold Watch", "Rs. 9,500", "women_20"));
        productList.add(new ProductItem("Graphic T-Shirt", "Rs. 1,290", "men_2"));
        productList.add(new ProductItem("Printed T-Shirt", "Rs. 990", "kids_1"));
        productList.add(new ProductItem("Lawn Kurta Set", "Rs. 4,990", "all_4"));
        productList.add(new ProductItem("Active T-Shirt", "Rs. 1,190", "men_7"));
        productList.add(new ProductItem("High-Waist Jeggings", "Rs. 2,990", "women_17"));
        productList.add(new ProductItem("Analog Watch", "Rs. 15,500", "all_13"));
        productList.add(new ProductItem("Kids Polo Shirt", "Rs. 2,790", "kids_10"));
        productList.add(new ProductItem("Running Shoes", "Rs. 4,990", "men_12"));
        productList.add(new ProductItem("Solid Lawn Kurta", "Rs. 2,490", "all_8"));
        productList.add(new ProductItem("Ethnic Khussa", "Rs. 1,990", "women_6"));
        productList.add(new ProductItem("Superhero T-Shirt", "Rs. 1,290", "kids_19"));
        productList.add(new ProductItem("Straight Fit Jeans", "Rs. 2,990", "men_20"));
        productList.add(new ProductItem("Solid Casual Top", "Rs. 1,790", "women_12"));
        productList.add(new ProductItem("Active T-Shirt", "Rs. 1,190", "all_11"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 2,200", "men_9"));
        productList.add(new ProductItem("Denim Jeans", "Rs. 1,490", "kids_2"));
        productList.add(new ProductItem("Block Heeled Sandals", "Rs. 3,200", "all_10"));
        productList.add(new ProductItem("Women's Sneakers", "Rs. 5,490", "women_14"));
        productList.add(new ProductItem("Leather Wallet", "Rs. 1,800", "men_13"));
        productList.add(new ProductItem("Floral Print Kurti", "Rs. 2,890", "all_2"));
        productList.add(new ProductItem("Building Blocks", "Rs. 4,500", "kids_12"));
        productList.add(new ProductItem("Liquid Foundation", "Rs. 2,150", "women_10"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 2,200", "all_15"));
        productList.add(new ProductItem("Casual Check Shirt", "Rs. 2,190", "men_1"));
        productList.add(new ProductItem("Girls Kurta", "Rs. 2,490", "kids_7"));
        productList.add(new ProductItem("Printed Lawn Saree", "Rs. 5,500", "women_7"));
        productList.add(new ProductItem("Men's Sneakers", "Rs. 7,490", "all_3"));
        productList.add(new ProductItem("Striped Shirt", "Rs. 2,390", "men_11"));
        productList.add(new ProductItem("Skinny Fit Jeans", "Rs. 3,990", "all_12"));
        productList.add(new ProductItem("Women's Wallet", "Rs. 1,990", "women_19"));
        productList.add(new ProductItem("Boys Shalwar Kameez", "Rs. 3,200", "kids_15"));
        productList.add(new ProductItem("Digital Watch", "Rs. 12,500", "men_18"));
        productList.add(new ProductItem("Printed Lawn Saree", "Rs. 5,500", "all_16"));
        productList.add(new ProductItem("Formal Cotton Shirt", "Rs. 2,500", "men_6"));
        productList.add(new ProductItem("Block Heeled Sandals", "Rs. 3,200", "women_8"));
        productList.add(new ProductItem("Graphic Hoodie", "Rs. 1,990", "kids_3"));
        productList.add(new ProductItem("Leather Boots", "Rs. 6,500", "all_17"));
        productList.add(new ProductItem("Walking Shoes", "Rs. 6,200", "women_18"));
        productList.add(new ProductItem("Printed Relaxed Fit", "Rs. 1,850", "men_5"));
        productList.add(new ProductItem("Graphic T-Shirt", "Rs. 1,290", "all_5"));
        productList.add(new ProductItem("Baby Stroller", "Rs. 7,500", "kids_16"));
        productList.add(new ProductItem("Skinny Fit Jeans", "Rs. 3,990", "women_9"));
        productList.add(new ProductItem("Digital Watch", "Rs. 12,500", "all_19"));
        productList.add(new ProductItem("Slim Fit Jeans", "Rs. 3,200", "men_4"));
        productList.add(new ProductItem("Toy Car Set", "Rs. 1,490", "kids_9"));
        productList.add(new ProductItem("Chikankari Kurti", "Rs. 3,190", "women_15"));
        productList.add(new ProductItem("Embroidered Suit", "Rs. 7,990", "all_6"));
        productList.add(new ProductItem("Printed T-Shirt", "Rs. 1,450", "men_14"));
        productList.add(new ProductItem("Crossbody Handbag", "Rs. 4,200", "women_13"));
        productList.add(new ProductItem("Kids Clogs", "Rs. 6,490", "kids_4"));
        productList.add(new ProductItem("Slim Fit Jeans", "Rs. 3,200", "all_7"));
        productList.add(new ProductItem("Solid Lawn Kurta", "Rs. 2,490", "women_4"));
        productList.add(new ProductItem("Men's Kurta", "Rs. 2,800", "men_16"));
        productList.add(new ProductItem("Cartoon Backpack", "Rs. 2,990", "kids_17"));
        productList.add(new ProductItem("Hyaluronic Serum", "Rs. 2,800", "women_16"));
        productList.add(new ProductItem("Ethnic Khussa", "Rs. 1,990", "all_14"));
        productList.add(new ProductItem("Leather Boots", "Rs. 6,500", "men_10"));
        productList.add(new ProductItem("Fashion Doll", "Rs. 2,800", "kids_6"));
        productList.add(new ProductItem("Lawn Kurta Set", "Rs. 4,990", "women_2"));
        productList.add(new ProductItem("Polo T-Shirt", "Rs. 1,790", "men_17"));
        productList.add(new ProductItem("Rose Gold Watch", "Rs. 9,500", "all_20"));
        productList.add(new ProductItem("Casual Printed Top", "Rs. 1,290", "women_5"));
        productList.add(new ProductItem("Boys Kurta", "Rs. 2,200", "kids_8"));
        productList.add(new ProductItem("Analog Watch", "Rs. 15,500", "men_8"));
        productList.add(new ProductItem("Girls Sandals", "Rs. 1,700", "kids_14"));
        productList.add(new ProductItem("Embroidered Suit", "Rs. 7,990", "women_3"));
        productList.add(new ProductItem("Cargo Trousers", "Rs. 3,400", "men_19"));
        productList.add(new ProductItem("Full Sleeve Shirt", "Rs. 1,350", "kids_13"));
        productList.add(new ProductItem("Kids Sneakers", "Rs. 5,990", "kids_11"));
        productList.add(new ProductItem("Girls Lehenga Choli", "Rs. 4,200", "kids_18"));

    }
}



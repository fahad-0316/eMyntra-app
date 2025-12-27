package com.example.emyntra;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchProductAdapter adapter;
    private List<ProductItem> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView tvQuery = findViewById(R.id.tv_search_query);
        recyclerView = findViewById(R.id.rv_search_results);

        // Get the search query passed from MainActivity
        String query = getIntent().getStringExtra("SEARCH_QUERY");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (query != null && !query.isEmpty()) {
            tvQuery.setText("Results for \"" + query + "\"");
        } else {
            tvQuery.setText("Search Results");
        }

        // Setup Recycler View with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadRandomSearchData();

        adapter = new SearchProductAdapter(this, searchList);
        recyclerView.setAdapter(adapter);
    }

    private void loadRandomSearchData() {
        // Create a pool of items (using items we created before)
        List<ProductItem> allItemsPool = new ArrayList<>();

        // Add items from Men
        allItemsPool.add(new ProductItem("Casual Check Shirt", "Rs. 2,190", "men_1"));
        allItemsPool.add(new ProductItem("Men's Sneakers", "Rs. 7,490", "men_3"));
        allItemsPool.add(new ProductItem("Analog Watch", "Rs. 15,500", "men_8"));
        allItemsPool.add(new ProductItem("Slim Fit Jeans", "Rs. 3,200", "men_4"));
        allItemsPool.add(new ProductItem("Leather Boots", "Rs. 6,500", "men_10"));

        // Add items from Women
        allItemsPool.add(new ProductItem("Floral Print Kurti", "Rs. 2,890", "women_1"));
        allItemsPool.add(new ProductItem("Lawn Kurta Set", "Rs. 4,990", "women_2"));
        allItemsPool.add(new ProductItem("Crossbody Handbag", "Rs. 4,200", "women_13"));
        allItemsPool.add(new ProductItem("Embroidered Suit", "Rs. 7,990", "women_3"));
        allItemsPool.add(new ProductItem("Women's Sneakers", "Rs. 5,490", "women_14"));

        // Add items from Kids
        allItemsPool.add(new ProductItem("Printed T-Shirt", "Rs. 990", "kids_1"));
        allItemsPool.add(new ProductItem("Kids Sneakers", "Rs. 5,990", "kids_11"));
        allItemsPool.add(new ProductItem("Toy Car Set", "Rs. 1,490", "kids_9"));
        allItemsPool.add(new ProductItem("Building Blocks", "Rs. 4,500", "kids_12"));
        allItemsPool.add(new ProductItem("Baby Stroller", "Rs. 7,500", "kids_16"));

        // Shuffle to simulate "Random" search results based on user query (simulated)
        Collections.shuffle(allItemsPool);

        // Take first 10 items
        searchList = new ArrayList<>();
        for (int i = 0; i < 10 && i < allItemsPool.size(); i++) {
            searchList.add(allItemsPool.get(i));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
package com.example.k23411teapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.ProductAdapter;
import com.example.models.Category;
import com.example.models.DataWareHouse;
import com.example.models.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ListView lvProduct;
    TextView tvCategoryName;
    ArrayList<Product> products;
    ProductAdapter productAdapter;
    Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tvCategoryName), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentData();
        addViews();
        loadData();
    }

    private void getIntentData() {
        if (getIntent().hasExtra("CATEGORY")) {
            currentCategory = (Category) getIntent().getSerializableExtra("CATEGORY");
        }
    }

    private void addViews() {
        lvProduct = findViewById(R.id.lvProduct);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        
        if (currentCategory != null) {
            tvCategoryName.setText("Sản phẩm: " + currentCategory.getCategoryName());
        } else {
            tvCategoryName.setText("Tất cả sản phẩm");
        }

        productAdapter = new ProductAdapter(this, R.layout.item_custom_product);
        lvProduct.setAdapter(productAdapter);
    }

    private void loadData() {
        products = new ArrayList<>();
        ArrayList<Product> allProducts = DataWareHouse.getProducts();
        
        if (currentCategory != null) {
            for (Product p : allProducts) {
                if (p.getCategoryId().equals(currentCategory.getCategoryId())) {
                    products.add(p);
                }
            }
        } else {
            products.addAll(allProducts);
        }

        productAdapter.addAll(products);
    }
}

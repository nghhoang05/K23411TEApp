package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.FbProductAdapter;
import com.example.database.CartDatabaseHelper;
import com.example.fb.CartItem;
import com.example.fb.FbProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG    = "ProductList";
    private static final String DB_URL = "https://k23411te-default-rtdb.asia-southeast1.firebasedatabase.app";

    private String categoryId, categoryName;

    private ProgressBar progressProducts;
    private RecyclerView rvProducts;
    private TextView tvCategoryTitle, tvProductCount, tvEmptyProducts, btnCart;
    private View layoutEmptyProducts;
    private EditText etSearch;

    private final List<FbProduct> allProducts      = new ArrayList<>();
    private final List<FbProduct> filteredProducts = new ArrayList<>();
    private FbProductAdapter adapter;
    private CartDatabaseHelper cartDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        categoryId   = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");

        cartDb = new CartDatabaseHelper(this);
        bindViews();
        loadProducts();
    }

    private void bindViews() {
        progressProducts    = findViewById(R.id.progressProducts);
        rvProducts          = findViewById(R.id.rvProducts);
        tvCategoryTitle     = findViewById(R.id.tvCategoryTitle);
        tvProductCount      = findViewById(R.id.tvProductCount);
        layoutEmptyProducts = findViewById(R.id.layoutEmptyProducts);
        tvEmptyProducts     = findViewById(R.id.tvEmptyProducts);
        btnCart             = findViewById(R.id.btnCart);
        etSearch            = findViewById(R.id.etSearch);

        tvCategoryTitle.setText(categoryName != null ? categoryName : "Sản phẩm");

        adapter = new FbProductAdapter(this, filteredProducts);
        adapter.setOnItemListener(new FbProductAdapter.OnItemListener() {
            @Override public void onProductClick(FbProduct p) {
                Intent i = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                i.putExtra("product", p);
                startActivity(i);
            }
            @Override public void onAddToCart(FbProduct p) {
                addToCart(p, 1);
            }
        });

        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(adapter);

        // Cart button
        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        // Search filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int co, int af) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int st, int be, int co) {
                filterProducts(s.toString());
            }
        });
    }

    private void loadProducts() {
        progressProducts.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance(DB_URL)
                .getReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        allProducts.clear();
                        for (DataSnapshot c : snap.getChildren()) {
                            String catId = c.child("categoryId").getValue(String.class);
                            if (categoryId != null && !categoryId.equals(catId)) continue;

                            Boolean active = c.child("isActive").getValue(Boolean.class);
                            if (active != null && !active) continue;

                            FbProduct p = new FbProduct();
                            p.setKey(c.getKey());
                            p.setProductName(c.child("productName").getValue(String.class));
                            p.setCategoryId(catId);
                            Object pr = c.child("price").getValue();
                            p.setPrice(pr != null ? ((Number) pr).doubleValue() : 0);
                            Object st = c.child("stock").getValue();
                            p.setStock(st != null ? ((Number) st).longValue() : 0);
                            p.setImageUrl(c.child("imageUrl").getValue(String.class));
                            p.setActive(true);
                            allProducts.add(p);
                        }
                        progressProducts.setVisibility(View.GONE);
                        filterProducts(etSearch.getText().toString());
                        updateCartBadge();
                    }
                    @Override public void onCancelled(DatabaseError e) {
                        progressProducts.setVisibility(View.GONE);
                        Log.e(TAG, "products error", e.toException());
                    }
                });
    }

    private void filterProducts(String query) {
        filteredProducts.clear();
        String q = query.toLowerCase().trim();
        for (FbProduct p : allProducts) {
            if (q.isEmpty() || (p.getProductName() != null && p.getProductName().toLowerCase().contains(q))) {
                filteredProducts.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        tvProductCount.setText(filteredProducts.size() + " sản phẩm");

        if (filteredProducts.isEmpty()) {
            layoutEmptyProducts.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
        } else {
            layoutEmptyProducts.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
        }
    }

    private void addToCart(FbProduct p, int qty) {
        CartItem item = new CartItem(p.getKey(), p.getProductName(), p.getPrice(), qty, p.getImageUrl());
        cartDb.addOrUpdateItem(item);
        Toast.makeText(this, "✅ Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        updateCartBadge();
    }

    private void updateCartBadge() {
        int qty = cartDb.getTotalQuantity();
        btnCart.setText("🛒 " + qty);
    }

    @Override protected void onResume() {
        super.onResume();
        updateCartBadge();
    }
}

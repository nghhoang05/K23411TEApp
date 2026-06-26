package com.example.k23411teapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.database.CartDatabaseHelper;
import com.example.fb.CartItem;
import com.example.fb.FbProduct;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private FbProduct product;
    private CartDatabaseHelper cartDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = (FbProduct) getIntent().getSerializableExtra("product");
        cartDb  = new CartDatabaseHelper(this);

        if (product == null) { finish(); return; }

        // Bind views
        ImageView img       = findViewById(R.id.imgProduct);
        TextView tvName     = findViewById(R.id.tvDetailName);
        TextView tvPrice    = findViewById(R.id.tvDetailPrice);
        TextView tvStock    = findViewById(R.id.tvDetailStock);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvQty      = findViewById(R.id.tvDetailQty);
        Button   btnMinus   = findViewById(R.id.btnMinus);
        Button   btnPlus    = findViewById(R.id.btnPlus);
        Button   btnAdd     = findViewById(R.id.btnAddToCart);

        // Populate
        tvName.setText(product.getProductName());
        tvPrice.setText(formatPrice(product.getPrice()));
        tvStock.setText(product.getStock() + " còn lại");
        tvCategory.setText("📦 " + (product.getCategoryId() != null ? product.getCategoryId() : ""));
        tvQty.setText(String.valueOf(quantity));

        // Load image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(this).load(product.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop().into(img);
        }

        // Quantity controls
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) { quantity--; tvQty.setText(String.valueOf(quantity)); }
        });
        btnPlus.setOnClickListener(v -> {
            if (quantity < product.getStock()) { quantity++; tvQty.setText(String.valueOf(quantity)); }
            else Toast.makeText(this, "Đã đạt tối đa tồn kho!", Toast.LENGTH_SHORT).show();
        });

        // Add to Cart
        btnAdd.setOnClickListener(v -> {
            CartItem item = new CartItem(
                    product.getKey(), product.getProductName(),
                    product.getPrice(), quantity, product.getImageUrl());
            cartDb.addOrUpdateItem(item);
            Toast.makeText(this, "✅ Đã thêm " + quantity + " sản phẩm vào giỏ!", Toast.LENGTH_SHORT).show();
        });
    }

    private String formatPrice(double p) {
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)p) + " ₫";
    }
}

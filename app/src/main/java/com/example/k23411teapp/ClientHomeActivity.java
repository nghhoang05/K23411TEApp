package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.FbCategoryAdapter;
import com.example.fb.FbCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientHomeActivity extends AppCompatActivity {

    private static final String TAG    = "ClientHome";
    private static final String DB_URL = "https://k23411te-default-rtdb.asia-southeast1.firebasedatabase.app";

    private ProgressBar progressHome;
    private RecyclerView rvCategories;
    private final List<FbCategory> categoryList = new ArrayList<>();
    private FbCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        progressHome = findViewById(R.id.progressHome);
        rvCategories = findViewById(R.id.rvCategories);

        adapter = new FbCategoryAdapter(this, categoryList);
        adapter.setOnItemClickListener(cat -> {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra("categoryId",   cat.getKey());
            intent.putExtra("categoryName", cat.getCategoryName());
            startActivity(intent);
        });

        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategories.setAdapter(adapter);

        loadCategories();
    }

    private void loadCategories() {
        FirebaseDatabase.getInstance(DB_URL)
                .getReference("categories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        categoryList.clear();
                        for (DataSnapshot c : snap.getChildren()) {
                            FbCategory cat = new FbCategory();
                            cat.setKey(c.getKey());
                            cat.setCategoryName(c.child("categoryName").getValue(String.class));
                            cat.setDescription(c.child("description").getValue(String.class));
                            categoryList.add(cat);
                        }
                        adapter.notifyDataSetChanged();
                        progressHome.setVisibility(View.GONE);
                        rvCategories.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Categories loaded: " + categoryList.size());
                    }
                    @Override
                    public void onCancelled(DatabaseError e) {
                        Log.e(TAG, "categories err", e.toException());
                        progressHome.setVisibility(View.GONE);
                    }
                });
    }
}

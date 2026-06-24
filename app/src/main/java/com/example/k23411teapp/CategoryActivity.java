package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DALS.CategoryDAO;
import com.example.adapters.CategoryAdapter;
import com.example.models.Category;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ListView lvCategory;
    ArrayList<Category> categories;
    CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory=categories.get(position);
                long result=CategoryDAO.removeNewCategory(CategoryActivity.this,selectedCategory);
                if (result>0) {
                    categories = CategoryDAO.getCategories(CategoryActivity.this);
                    categoryAdapter.clear();
                    categoryAdapter.addAll(categories);
                    categoryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void addViews() {
        lvCategory=findViewById(R.id.lvCategory);
        
        processCopy();

        String dbPath = getDatabasePath(CategoryDAO.DATABASE_NAME).getAbsolutePath();
        android.widget.Toast.makeText(this, "DB Path: " + dbPath, android.widget.Toast.LENGTH_LONG).show();
        android.util.Log.d("DATABASE_DEBUG", "DB Path: " + dbPath);

        categories= CategoryDAO.getCategories(this);
        categoryAdapter=new CategoryAdapter(this,R.layout.category_custom_item);
        categoryAdapter.addAll(categories);
        lvCategory.setAdapter(categoryAdapter);
        
        lvCategory.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                Category selectedCategory = categories.get(position);
                android.content.Intent intent = new android.content.Intent(CategoryActivity.this, ProductActivity.class);
                intent.putExtra("CATEGORY", selectedCategory);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnu_category_new){
            Intent intent=new Intent(this,CategoryNewActivity.class);
            startActivityForResult(intent, 1);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==2)
        {
            categories=CategoryDAO.getCategories(this);
            categoryAdapter.clear();
            categoryAdapter.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void processCopy() {
        java.io.File dbFile = getDatabasePath(CategoryDAO.DATABASE_NAME);
        // If the file does not exist, or it is too small (blank DB created by openOrCreateDatabase)
        if (!dbFile.exists() || dbFile.length() < 10000) {
            try {
                if (dbFile.exists()) {
                    deleteDatabase(CategoryDAO.DATABASE_NAME); // Safely deletes db, -wal, and -shm files
                }
                CopyDataBaseFromAsset();
                android.util.Log.d("DATABASE_DEBUG", "Database copied from assets");
            } catch (Exception e) {
                android.util.Log.e("DATABASE_DEBUG", e.toString());
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try {
            java.io.InputStream myInput = getAssets().open(CategoryDAO.DATABASE_NAME);
            java.io.File dbFile = getDatabasePath(CategoryDAO.DATABASE_NAME);
            java.io.File dbDir = dbFile.getParentFile();
            if (dbDir != null && !dbDir.exists()) {
                dbDir.mkdirs();
            }
            java.io.OutputStream myOutput = new java.io.FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
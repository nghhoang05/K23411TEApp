package com.example.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.models.Category;

/**
 * Adapter cũ dùng cho CategoryActivity (ListView + SQLite local).
 * KHÔNG phải adapter Firebase — xem FbCategoryAdapter cho Firebase.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

    Activity context;
    int resource;

    public CategoryAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View custom = inflater.inflate(resource, null);
        Category cate = getItem(position);
        TextView txtCateId   = custom.findViewById(R.id.txtCategoryId);
        TextView txtCateName = custom.findViewById(R.id.txtCategoryName);
        TextView txtDesc     = custom.findViewById(R.id.txtDescription);
        txtCateId.setText(cate.getCategoryId() + "");
        txtCateName.setText(cate.getCategoryName() + "");
        txtDesc.setText(cate.getDescription() + "");
        return custom;
    }
}

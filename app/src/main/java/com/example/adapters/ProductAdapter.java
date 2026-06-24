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
import com.example.models.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private int resource;

    public ProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);
        }

        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
        TextView tvProductQuantity = convertView.findViewById(R.id.tvProductQuantity);

        Product product = getItem(position);
        if (product != null) {
            tvProductName.setText(product.getProductName());
            tvProductPrice.setText("Giá: " + String.format("%,.0f", product.getPrice()) + " đ");
            tvProductQuantity.setText("Số lượng: " + product.getQuantity());
        }

        return convertView;
    }
}

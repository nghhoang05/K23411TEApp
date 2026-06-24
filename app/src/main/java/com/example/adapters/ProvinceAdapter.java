package com.example.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.models.Province;

import java.util.List;

public class ProvinceAdapter extends ArrayAdapter<Province> {
    private Activity context;
    private int resource;

    public ProvinceAdapter(@NonNull Activity context, int resource, @NonNull List<Province> objects) {
        super(context, resource, objects);
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

        TextView tvProvinceName = convertView.findViewById(R.id.tvProvinceName);
        Province province = getItem(position);

        if (province != null) {
            tvProvinceName.setText(province.getName());
        }

        return convertView;
    }
}

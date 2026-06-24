package com.example.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.models.Employee;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Activity context;
    int resource;

    public EmployeeAdapter(@NonNull Activity context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom_view = convertView;
        if (custom_view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            custom_view = inflater.inflate(this.resource, parent, false);
        }

        Employee emp = getItem(position);

        if (emp != null) {
            TextView txtId = custom_view.findViewById(R.id.txtId);
            TextView txtName = custom_view.findViewById(R.id.txtName);
            TextView txtPhone = custom_view.findViewById(R.id.txtPhone);
            ImageView imgCall = custom_view.findViewById(R.id.imgCall);
            ImageView imgSms = custom_view.findViewById(R.id.imgSms);

            txtId.setText(emp.getId());
            txtName.setText(emp.getName());
            txtPhone.setText(emp.getPhone());

            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + emp.getPhone()));
                    context.startActivity(intentCall);
                }
            });

            imgSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSms = new Intent(Intent.ACTION_SENDTO);
                    intentSms.setData(Uri.parse("smsto:" + emp.getPhone()));
                    context.startActivity(intentSms);
                }
            });
        }

        // Must return the custom_view we prepared, NOT super.getView()
        return custom_view;
    }
}

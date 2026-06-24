package com.example.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.models.DataWareHouse;
import com.example.models.Order;
import com.example.models.OrderStatus;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends ArrayAdapter<Order> {
    private Context context;
    private int resource;
    private List<Order> objects;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Order order = objects.get(position);

        if (order != null) {
            TextView txtOrderId = item.findViewById(R.id.txtOrderId);
            TextView txtOrderDate = item.findViewById(R.id.txtOrderDate);
            TextView txtOrderTotal = item.findViewById(R.id.txtOrderTotal);
            Chip chipStatus = item.findViewById(R.id.chipStatus);

            // Hiển thị Mã đơn và Tên nhân viên
            String empName = DataWareHouse.getEmployeeNameById(order.getEmployeeId());
            txtOrderId.setText(String.format("%s - %s", order.getOrderId(), empName));

            txtOrderDate.setText(sdf.format(order.getOrderDate()));

            double total = DataWareHouse.sumOfMoneyForOrder(order);
            txtOrderTotal.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", total));

            OrderStatus status = order.getOrderStatus();
            if (status != null) {
                chipStatus.setText(status.toString());
                
                int bgColor, textColor;
                switch (status) {
                    case COMPLETED:
                        bgColor = Color.parseColor("#E8F5E9");
                        textColor = Color.parseColor("#2E7D32");
                        break;
                    case NOT_PAYMENT:
                        bgColor = Color.parseColor("#FFEBEE");
                        textColor = Color.parseColor("#C62828");
                        break;
                    case ON_LOGISTIC:
                        bgColor = Color.parseColor("#E3F2FD");
                        textColor = Color.parseColor("#1565C0");
                        break;
                    case COMPLAINT:
                        bgColor = Color.parseColor("#FFF3E0");
                        textColor = Color.parseColor("#EF6C00");
                        break;
                    default:
                        bgColor = Color.parseColor("#F5F5F5");
                        textColor = Color.parseColor("#757575");
                        break;
                }
                chipStatus.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(bgColor));
                chipStatus.setTextColor(textColor);
                chipStatus.setChipStrokeWidth(0);
            }
        }

        return item;
    }
}

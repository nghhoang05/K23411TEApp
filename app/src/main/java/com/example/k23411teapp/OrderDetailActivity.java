package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.models.DataWareHouse;
import com.example.models.Order;
import com.example.models.OrderDetail;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    TextView txtDetailOrderId, txtDetailCustomer, txtDetailEmployee, txtDetailDate, txtDetailStatus, txtDetailTotal;
    LinearLayout containerOrderItems;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        addViews();
        loadOrderDetails();
    }

    private void addViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.str_order_details);
        }

        txtDetailOrderId = findViewById(R.id.txtDetailOrderId);
        txtDetailCustomer = findViewById(R.id.txtDetailCustomer);
        txtDetailEmployee = findViewById(R.id.txtDetailEmployee);
        txtDetailDate = findViewById(R.id.txtDetailDate);
        txtDetailStatus = findViewById(R.id.txtDetailStatus);
        txtDetailTotal = findViewById(R.id.txtDetailTotal);
        containerOrderItems = findViewById(R.id.containerOrderItems);
    }

    private void loadOrderDetails() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDER")) {
            Order order = (Order) intent.getSerializableExtra("ORDER");
            if (order != null) {
                txtDetailOrderId.setText(String.format("Order ID: %s", order.getOrderId()));
                txtDetailCustomer.setText(String.format("Customer ID: %s", order.getCustomerId()));
                txtDetailEmployee.setText(String.format("Employee: %s", DataWareHouse.getEmployeeNameById(order.getEmployeeId())));
                txtDetailDate.setText(String.format("Date: %s", sdf.format(order.getOrderDate())));
                txtDetailStatus.setText(String.format("Status: %s", order.getOrderStatus().toString()));
                
                double total = DataWareHouse.sumOfMoneyForOrder(order);
                txtDetailTotal.setText(String.format(Locale.getDefault(), "Total: %,.0f VNĐ", total));

                // Clear container before adding items
                containerOrderItems.removeAllViews();
                ArrayList<OrderDetail> allDetails = DataWareHouse.getOrderDetails();
                for (OrderDetail detail : allDetails) {
                    if (detail.getOrderId().equals(order.getOrderId())) {
                        TextView itemText = new TextView(this);
                        itemText.setPadding(0, 8, 0, 8);
                        double lineTotal = detail.getQuantity() * detail.getPrice() * (1 - detail.getCoupon()) * (1 + detail.getVAT());
                        String detailInfo = String.format(Locale.getDefault(),
                            "Detail ID: %s | Qty: %d | Price: %,.0f | Line Total: %,.0f VNĐ",
                            detail.getOrderDetailId(), detail.getQuantity(), detail.getPrice(), lineTotal);
                        itemText.setText(detailInfo);
                        containerOrderItems.addView(itemText);
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
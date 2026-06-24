package com.example.k23411teapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import androidx.appcompat.widget.SearchView;
import com.example.adapters.OrderAdapter;
import com.example.models.DataWareHouse;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderManagementActivity extends AppCompatActivity {

    TextView txtFromDate, txtToDate;
    ImageView imgFromDate, imgToDate;
    MaterialButton btnClearFilter, btnFilter;
    ListView lvOrder;
    ArrayList<Order> orders;
    OrderAdapter orderAdapter;
    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    Calendar calFromDate=Calendar.getInstance();
    Calendar calToDate=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener fromDateListener;
    DatePickerDialog.OnDateSetListener toDateListener;



    private OrderStatus currentSelectedStatus = OrderStatus.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_management);

        // Sync initial calendars with UI defaults (01/01/2024 - 31/12/2024)
        calFromDate.set(2024, Calendar.JANUARY, 1);
        calToDate.set(2024, Calendar.DECEMBER, 31);
        
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.str_order_management);
        }

        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        fromDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calFromDate.set(Calendar.YEAR, year);
                calFromDate.set(Calendar.MONTH, month);
                calFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txtFromDate.setText(sdf.format(calFromDate.getTime()));
            }
        };
        toDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calToDate.set(Calendar.YEAR, year);
                calToDate.set(Calendar.MONTH, month);
                calToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txtToDate.setText(sdf.format(calToDate.getTime()));
            }
        };

        imgFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker=new DatePickerDialog(OrderManagementActivity.this,fromDateListener,calFromDate.get(Calendar.YEAR),calFromDate.get(Calendar.MONTH),calFromDate.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });
        imgToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker=new DatePickerDialog(OrderManagementActivity.this,toDateListener,calToDate.get(Calendar.YEAR),calToDate.get(Calendar.MONTH),calToDate.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });
        btnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset Date Calendars
                calFromDate.set(2024, Calendar.JANUARY, 1);
                calToDate.set(2024, Calendar.DECEMBER, 31);
                
                // Update UI Texts
                txtFromDate.setText(sdf.format(calFromDate.getTime()));
                txtToDate.setText(sdf.format(calToDate.getTime()));
                
                // Reset Status
                currentSelectedStatus = OrderStatus.ALL;
                
                // Show all orders
                showOrders(DataWareHouse.getOrders());
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date fromDate = calFromDate.getTime();
                Date toDate = calToDate.getTime();
                showOrders(DataWareHouse.filterOrders(fromDate, toDate, currentSelectedStatus));
            }
        });
    }

    private void addViews() {
        txtFromDate=findViewById(R.id.txtFromDate);
        txtToDate=findViewById(R.id.txtToDate);
        imgFromDate=findViewById(R.id.imgFromDate);
        imgToDate=findViewById(R.id.imgToDate);
        btnClearFilter=findViewById(R.id.btnClearFilter);
        btnFilter=findViewById(R.id.btnFilter);
        lvOrder=findViewById(R.id.lvOrder);

        // Khởi tạo text hiển thị đồng bộ với Calendar mặc định
        txtFromDate.setText(sdf.format(calFromDate.getTime()));
        txtToDate.setText(sdf.format(calToDate.getTime()));
        
        // Khởi tạo danh sách theo bộ lọc mặc định (Năm 2024)
        orders = new ArrayList<>(DataWareHouse.filterOrders(calFromDate.getTime(), calToDate.getTime(), currentSelectedStatus));
        orderAdapter=new OrderAdapter(this, R.layout.item_order, orders);
        lvOrder.setAdapter(orderAdapter);
        
        lvOrder.setOnItemClickListener((parent, view, position, id) -> {
            Order selectedOrder = orders.get(position);
            Intent intent = new Intent(OrderManagementActivity.this, OrderDetailActivity.class);
            intent.putExtra("ORDER", selectedOrder);
            startActivity(intent);
        });
    }

    private void showOrders(ArrayList<Order> newList) {
        orderAdapter.clear();
        orderAdapter.addAll(newList);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu_status, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void processSearch(String query) {
        ArrayList<Order> filteredList = new ArrayList<>();
        for (Order o : DataWareHouse.getOrders()) {
            if (o.getOrderId().toLowerCase().contains(query.toLowerCase()) ||
                    o.getCustomerId().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(o);
            }
        }
        showOrders(filteredList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_order_status_all) {
            currentSelectedStatus = OrderStatus.ALL;
        } else if (itemId == R.id.menu_status_completed) {
            currentSelectedStatus = OrderStatus.COMPLETED;
        } else if (itemId == R.id.menu_status_not_payment) {
            currentSelectedStatus = OrderStatus.NOT_PAYMENT;
        } else if (itemId == R.id.menu_status_on_logistic) {
            currentSelectedStatus = OrderStatus.ON_LOGISTIC;
        } else if (itemId == R.id.menu_order_status_complaint) {
            currentSelectedStatus = OrderStatus.COMPLAINT;
        } else {
            return super.onOptionsItemSelected(item);
        }

        // Apply filtering using BOTH status and date range
        Date fromDate = calFromDate.getTime();
        Date toDate = calToDate.getTime();
        showOrders(DataWareHouse.filterOrders(fromDate, toDate, currentSelectedStatus));
        return true;
    }

}
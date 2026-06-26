package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.TopCustomerAdapter;
import com.example.adapters.TopProductAdapter;
import com.example.fb.FbCustomer;
import com.example.fb.FbOrder;
import com.example.fb.FbOrderDetail;
import com.example.fb.FbProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AdminDashboardActivity:
 *  - Load 4 node Firebase: orders, orderDetails, customers, products
 *  - Tính client-side: doanh thu, số đơn, TOP customers, TOP products
 */
public class AdminDashboardActivity extends AppCompatActivity {

    private static final String TAG    = "AdminDashboard";
    private static final String DB_URL = "https://k23411te-default-rtdb.asia-southeast1.firebasedatabase.app";
    private static final int    TOP_N  = 5;

    // UI
    private ProgressBar progressAdmin;
    private ScrollView  scrollAdmin;
    private TextView    tvSubtitle, tvRevenue, tvOrders, tvProducts, tvCustomers;
    private RecyclerView rvTopCustomers, rvTopProducts;

    // Data maps (loaded from Firebase)
    private final Map<String, FbOrder>       ordersMap       = new HashMap<>();
    private final Map<String, FbOrderDetail> orderDetailsMap = new HashMap<>();
    private final Map<String, FbCustomer>    customersMap    = new HashMap<>();
    private final Map<String, FbProduct>     productsMap     = new HashMap<>();

    private int dataLoadedCount = 0;
    private static final int TOTAL_LOADS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        bindViews();
        loadAllData();
    }

    private void bindViews() {
        progressAdmin  = findViewById(R.id.progressAdmin);
        scrollAdmin    = findViewById(R.id.scrollAdmin);
        tvSubtitle     = findViewById(R.id.tvAdminSubtitle);
        tvRevenue      = findViewById(R.id.tvTotalRevenue);
        tvOrders       = findViewById(R.id.tvTotalOrders);
        tvProducts     = findViewById(R.id.tvTotalProducts);
        tvCustomers    = findViewById(R.id.tvTotalCustomers);
        rvTopCustomers = findViewById(R.id.rvTopCustomers);
        rvTopProducts  = findViewById(R.id.rvTopProducts);

        rvTopCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvTopProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    // ──────────────────────────────────────────────
    //  Firebase Load (4 nodes song song)
    // ──────────────────────────────────────────────

    private void loadAllData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(DB_URL);

        // 1. orders
        db.getReference("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                for (DataSnapshot c : snap.getChildren()) {
                    FbOrder o = new FbOrder();
                    o.setKey(c.getKey());
                    o.setCustomerId(c.child("customerId").getValue(String.class));
                    o.setEmployeeId(c.child("employeeId").getValue(String.class));
                    o.setStatus(c.child("status").getValue(String.class));
                    Object amt = c.child("totalAmount").getValue();
                    o.setTotalAmount(amt != null ? ((Number) amt).doubleValue() : 0);
                    ordersMap.put(o.getKey(), o);
                }
                onOneLoaded("orders: " + ordersMap.size());
            }
            @Override public void onCancelled(DatabaseError e) { Log.e(TAG, "orders err", e.toException()); onOneLoaded("orders err"); }
        });

        // 2. orderDetails
        db.getReference("orderDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                for (DataSnapshot c : snap.getChildren()) {
                    FbOrderDetail d = new FbOrderDetail();
                    d.setKey(c.getKey());
                    d.setOrderId(c.child("orderId").getValue(String.class));
                    d.setProductId(c.child("productId").getValue(String.class));
                    Object qty = c.child("quantity").getValue();
                    d.setQuantity(qty != null ? ((Number) qty).longValue() : 0);
                    Object up = c.child("unitPrice").getValue();
                    d.setUnitPrice(up != null ? ((Number) up).doubleValue() : 0);
                    orderDetailsMap.put(d.getKey(), d);
                }
                onOneLoaded("orderDetails: " + orderDetailsMap.size());
            }
            @Override public void onCancelled(DatabaseError e) { Log.e(TAG, "orderDetails err", e.toException()); onOneLoaded("od err"); }
        });

        // 3. customers
        db.getReference("customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                for (DataSnapshot c : snap.getChildren()) {
                    FbCustomer cu = new FbCustomer();
                    cu.setKey(c.getKey());
                    cu.setFullName(c.child("fullName").getValue(String.class));
                    cu.setEmail(c.child("email").getValue(String.class));
                    customersMap.put(cu.getKey(), cu);
                }
                onOneLoaded("customers: " + customersMap.size());
            }
            @Override public void onCancelled(DatabaseError e) { Log.e(TAG, "customers err", e.toException()); onOneLoaded("cu err"); }
        });

        // 4. products
        db.getReference("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                for (DataSnapshot c : snap.getChildren()) {
                    FbProduct p = new FbProduct();
                    p.setKey(c.getKey());
                    p.setProductName(c.child("productName").getValue(String.class));
                    p.setCategoryId(c.child("categoryId").getValue(String.class));
                    Object pr = c.child("price").getValue();
                    p.setPrice(pr != null ? ((Number) pr).doubleValue() : 0);
                    Object st = c.child("stock").getValue();
                    p.setStock(st != null ? ((Number) st).longValue() : 0);
                    Boolean active = c.child("isActive").getValue(Boolean.class);
                    p.setActive(active != null && active);
                    productsMap.put(p.getKey(), p);
                }
                onOneLoaded("products: " + productsMap.size());
            }
            @Override public void onCancelled(DatabaseError e) { Log.e(TAG, "products err", e.toException()); onOneLoaded("pr err"); }
        });
    }

    private synchronized void onOneLoaded(String msg) {
        Log.d(TAG, "Loaded: " + msg);
        dataLoadedCount++;
        if (dataLoadedCount == TOTAL_LOADS) {
            runOnUiThread(this::computeAndDisplay);
        }
    }

    // ──────────────────────────────────────────────
    //  Compute Statistics
    // ──────────────────────────────────────────────

    private void computeAndDisplay() {
        // ── Stat Cards ──
        double totalRevenue = 0;
        for (FbOrder o : ordersMap.values()) totalRevenue += o.getTotalAmount();

        tvRevenue.setText(formatShort(totalRevenue));
        tvOrders.setText(String.valueOf(ordersMap.size()));
        tvProducts.setText(String.valueOf(productsMap.size()));
        tvCustomers.setText(String.valueOf(customersMap.size()));
        tvSubtitle.setText("Cập nhật lúc vừa xong • " + ordersMap.size() + " đơn");

        // ── TOP Customers ──
        Map<String, Double> spending  = new HashMap<>();
        Map<String, Integer> orderCnt = new HashMap<>();
        for (FbOrder o : ordersMap.values()) {
            if (o.getCustomerId() == null) continue;
            spending.merge(o.getCustomerId(), o.getTotalAmount(), Double::sum);
            orderCnt.merge(o.getCustomerId(), 1, Integer::sum);
        }
        List<TopCustomerAdapter.TopCustomerEntry> topCusts = new ArrayList<>();
        spending.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(TOP_N)
                .forEach(e -> {
                    TopCustomerAdapter.TopCustomerEntry entry = new TopCustomerAdapter.TopCustomerEntry();
                    entry.customerId    = e.getKey();
                    entry.totalSpending = e.getValue();
                    entry.orderCount    = orderCnt.getOrDefault(e.getKey(), 0);
                    FbCustomer cu = customersMap.get(e.getKey());
                    entry.customerName  = cu != null ? cu.getFullName() : e.getKey();
                    topCusts.add(entry);
                });
        rvTopCustomers.setAdapter(new TopCustomerAdapter(this, topCusts));

        // ── TOP Products ──
        Map<String, Long>   unitsSold    = new HashMap<>();
        Map<String, Double> prodRevenue  = new HashMap<>();
        for (FbOrderDetail d : orderDetailsMap.values()) {
            if (d.getProductId() == null) continue;
            unitsSold.merge(d.getProductId(), d.getQuantity(), Long::sum);
            prodRevenue.merge(d.getProductId(), d.getQuantity() * d.getUnitPrice(), Double::sum);
        }
        List<TopProductAdapter.TopProductEntry> topProds = new ArrayList<>();
        unitsSold.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(TOP_N)
                .forEach(e -> {
                    TopProductAdapter.TopProductEntry entry = new TopProductAdapter.TopProductEntry();
                    entry.productId    = e.getKey();
                    entry.unitsSold    = e.getValue();
                    entry.totalRevenue = prodRevenue.getOrDefault(e.getKey(), 0.0);
                    FbProduct pr = productsMap.get(e.getKey());
                    entry.productName  = pr != null ? pr.getProductName() : e.getKey();
                    topProds.add(entry);
                });
        rvTopProducts.setAdapter(new TopProductAdapter(this, topProds));

        // Show content
        progressAdmin.setVisibility(View.GONE);
        scrollAdmin.setVisibility(View.VISIBLE);
    }

    private String formatShort(double v) {
        if (v >= 1_000_000_000) return String.format("%.1fB ₫", v / 1_000_000_000);
        if (v >= 1_000_000)     return String.format("%.1fM ₫", v / 1_000_000);
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)v) + " ₫";
    }
}

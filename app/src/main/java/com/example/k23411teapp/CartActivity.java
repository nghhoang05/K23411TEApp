package com.example.k23411teapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.CartAdapter;
import com.example.database.CartDatabaseHelper;
import com.example.fb.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private CartDatabaseHelper cartDb;
    private CartAdapter adapter;
    private List<CartItem> cartItems;

    private RecyclerView rvCart;
    private LinearLayout layoutEmpty, layoutBottom;
    private TextView tvTotal, tvItemCount;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartDb = new CartDatabaseHelper(this);
        bindViews();
        loadCart();
    }

    private void bindViews() {
        rvCart        = findViewById(R.id.rvCart);
        layoutEmpty   = findViewById(R.id.layoutCartEmpty);
        layoutBottom  = findViewById(R.id.layoutCartBottom);
        tvTotal       = findViewById(R.id.tvCartTotal);
        tvItemCount   = findViewById(R.id.tvCartItemCount);
        btnCheckout   = findViewById(R.id.btnCheckout);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        btnCheckout.setOnClickListener(v -> checkout());
    }

    private void loadCart() {
        cartItems = cartDb.getAllItems();
        adapter = new CartAdapter(this, cartItems);
        adapter.setListener(new CartAdapter.OnCartActionListener() {
            @Override public void onIncrement(CartItem item, int pos) {
                cartDb.updateQuantity(item.getProductId(), item.getQuantity() + 1);
                cartItems.get(pos).setQuantity(item.getQuantity() + 1);
                adapter.notifyItemChanged(pos);
                updateTotal();
            }
            @Override public void onDecrement(CartItem item, int pos) {
                int newQty = item.getQuantity() - 1;
                if (newQty <= 0) {
                    cartDb.removeItem(item.getProductId());
                    cartItems.remove(pos);
                    adapter.notifyItemRemoved(pos);
                } else {
                    cartDb.updateQuantity(item.getProductId(), newQty);
                    cartItems.get(pos).setQuantity(newQty);
                    adapter.notifyItemChanged(pos);
                }
                updateTotal();
                checkEmpty();
            }
            @Override public void onDelete(CartItem item, int pos) {
                cartDb.removeItem(item.getProductId());
                cartItems.remove(pos);
                adapter.notifyItemRemoved(pos);
                updateTotal();
                checkEmpty();
            }
        });
        rvCart.setAdapter(adapter);
        checkEmpty();
        updateTotal();
    }

    private void updateTotal() {
        double total = cartDb.getTotal();
        tvTotal.setText(formatPrice(total));
        tvItemCount.setText(cartItems.size() + " loại sản phẩm");
    }

    private void checkEmpty() {
        boolean empty = cartItems.isEmpty();
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvCart.setVisibility(empty ? View.GONE : View.VISIBLE);
        layoutBottom.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void checkout() {
        cartDb.clearCart();
        cartItems.clear();
        adapter.notifyDataSetChanged();
        checkEmpty();
        Toast.makeText(this,
                "🎉 Đặt hàng thành công! Cảm ơn bạn đã mua hàng.",
                Toast.LENGTH_LONG).show();
    }

    private String formatPrice(double p) {
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)p) + " ₫";
    }
}

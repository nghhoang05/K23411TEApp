package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fb.CartItem;
import com.example.k23411teapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    private final Context context;
    private final List<CartItem> list;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onIncrement(CartItem item, int position);
        void onDecrement(CartItem item, int position);
        void onDelete(CartItem item, int position);
    }

    public CartAdapter(Context context, List<CartItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(OnCartActionListener l) { this.listener = l; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CartItem item = list.get(pos);
        h.tvName.setText(item.getProductName());
        h.tvPrice.setText(formatPrice(item.getPrice()));
        h.tvQty.setText(String.valueOf(item.getQuantity()));

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop().into(h.img);
        } else {
            h.img.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        h.btnPlus.setOnClickListener(v -> { if (listener != null) listener.onIncrement(item, h.getAdapterPosition()); });
        h.btnMinus.setOnClickListener(v -> { if (listener != null) listener.onDecrement(item, h.getAdapterPosition()); });
        h.btnDelete.setOnClickListener(v -> { if (listener != null) listener.onDelete(item, h.getAdapterPosition()); });
    }

    private String formatPrice(double p) {
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)p) + " ₫";
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvPrice, tvQty, btnDelete;
        android.widget.Button btnPlus, btnMinus;
        VH(View v) {
            super(v);
            img      = v.findViewById(R.id.imgCartItem);
            tvName   = v.findViewById(R.id.tvCartItemName);
            tvPrice  = v.findViewById(R.id.tvCartItemPrice);
            tvQty    = v.findViewById(R.id.tvCartItemQty);
            btnPlus  = v.findViewById(R.id.btnCartPlus);
            btnMinus = v.findViewById(R.id.btnCartMinus);
            btnDelete= v.findViewById(R.id.btnCartDelete);
        }
    }
}

package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fb.FbProduct;
import com.example.k23411teapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FbProductAdapter extends RecyclerView.Adapter<FbProductAdapter.VH> {

    private final Context context;
    private final List<FbProduct> list;
    private OnItemListener listener;

    public interface OnItemListener {
        void onProductClick(FbProduct product);
        void onAddToCart(FbProduct product);
    }

    public FbProductAdapter(Context context, List<FbProduct> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemListener(OnItemListener l) { this.listener = l; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_fb_product_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        FbProduct p = list.get(pos);

        h.tvName.setText(p.getProductName() != null ? p.getProductName() : "—");
        h.tvPrice.setText(formatPrice(p.getPrice()));

        // Load ảnh từ imageUrl bằng Glide
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(p.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(h.img);
        } else {
            h.img.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onProductClick(p); });
        h.btnAdd.setOnClickListener(v  -> { if (listener != null) listener.onAddToCart(p); });
    }

    private String formatPrice(double price) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format((long) price) + " ₫";
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvPrice;
        Button btnAdd;
        VH(View v) {
            super(v);
            img    = v.findViewById(R.id.imgProductCard);
            tvName = v.findViewById(R.id.tvProductCardName);
            tvPrice= v.findViewById(R.id.tvProductCardPrice);
            btnAdd = v.findViewById(R.id.btnAddCard);
        }
    }
}

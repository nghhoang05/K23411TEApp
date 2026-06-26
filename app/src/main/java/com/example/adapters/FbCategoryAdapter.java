package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fb.FbCategory;
import com.example.k23411teapp.R;

import java.util.List;

/** RecyclerView adapter cho danh mục sản phẩm (Firebase) */
public class FbCategoryAdapter extends RecyclerView.Adapter<FbCategoryAdapter.VH> {

    private static final String[] EMOJIS = {"📱","💻","⌚","📺","🎮","📷","🎧","🖨️","🔋","💡"};
    private static final int[] COLORS = {0xFF1E3A5F, 0xFF2E4A6F, 0xFF3E2A5F, 0xFF1E5F3A,
            0xFF5F3A1E, 0xFF1E4A5F, 0xFF5F1E3A, 0xFF2A5F1E, 0xFF4A1E5F, 0xFF1E5F4A};

    private final Context context;
    private final List<FbCategory> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onCategoryClick(FbCategory cat); }

    public FbCategoryAdapter(Context context, List<FbCategory> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.listener = l; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        FbCategory cat = list.get(pos);
        int idx = pos % EMOJIS.length;
        h.tvEmoji.setText(EMOJIS[idx]);
        h.tvName.setText(cat.getCategoryName() != null ? cat.getCategoryName() : "Danh mục");
        // Tô màu background card theo index
        h.itemView.setBackgroundColor(COLORS[idx]);
        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onCategoryClick(cat); });
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName;
        VH(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tvCategoryEmoji);
            tvName  = v.findViewById(R.id.tvCategoryName);
        }
    }
}

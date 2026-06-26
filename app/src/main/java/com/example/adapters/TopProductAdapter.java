package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k23411teapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.VH> {

    public static class TopProductEntry {
        public String productId;
        public String productName;
        public long   unitsSold;
        public double totalRevenue;
    }

    private static final int[] RANK_COLORS = {0xFFFFD700, 0xFFC0C0C0, 0xFFCD7F32, 0xFF8BA4BE, 0xFF8BA4BE};

    private final Context context;
    private final List<TopProductEntry> list;

    public TopProductAdapter(Context context, List<TopProductEntry> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_top_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        TopProductEntry e = list.get(pos);
        h.tvRank.setText("#" + (pos + 1));
        h.tvRank.getBackground().mutate().setTint(RANK_COLORS[Math.min(pos, RANK_COLORS.length - 1)]);
        h.tvName.setText(e.productName != null ? e.productName : e.productId);
        h.tvRevenue.setText("Doanh thu: " + formatPrice(e.totalRevenue));
        h.tvUnits.setText(String.valueOf(e.unitsSold));
    }

    private String formatPrice(double p) {
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)p) + " ₫";
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvRevenue, tvUnits;
        VH(View v) {
            super(v);
            tvRank   = v.findViewById(R.id.tvProductRank);
            tvName   = v.findViewById(R.id.tvProductTopName);
            tvRevenue= v.findViewById(R.id.tvProductRevenue);
            tvUnits  = v.findViewById(R.id.tvProductUnitsSold);
        }
    }
}

package com.example.adapters;

import android.content.Context;
import android.graphics.Color;
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
import java.util.Map;

/** Model nội bộ: entry (customerId, spending, orderCount, customerName) */
public class TopCustomerAdapter extends RecyclerView.Adapter<TopCustomerAdapter.VH> {

    public static class TopCustomerEntry {
        public String customerId;
        public String customerName;
        public double totalSpending;
        public int    orderCount;
    }

    private static final int[] RANK_COLORS = {0xFFFFD700, 0xFFC0C0C0, 0xFFCD7F32, 0xFF8BA4BE, 0xFF8BA4BE};

    private final Context context;
    private final List<TopCustomerEntry> list;

    public TopCustomerAdapter(Context context, List<TopCustomerEntry> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_top_customer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        TopCustomerEntry e = list.get(pos);
        h.tvRank.setText("#" + (pos + 1));
        h.tvRank.getBackground().mutate().setTint(RANK_COLORS[Math.min(pos, RANK_COLORS.length - 1)]);
        h.tvName.setText(e.customerName != null ? e.customerName : e.customerId);
        h.tvOrders.setText(e.orderCount + " đơn hàng");
        h.tvSpending.setText(formatPrice(e.totalSpending));
    }

    private String formatPrice(double p) {
        return NumberFormat.getNumberInstance(new Locale("vi","VN")).format((long)p) + " ₫";
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvOrders, tvSpending;
        VH(View v) {
            super(v);
            tvRank    = v.findViewById(R.id.tvCustomerRank);
            tvName    = v.findViewById(R.id.tvCustomerName);
            tvOrders  = v.findViewById(R.id.tvCustomerOrders);
            tvSpending= v.findViewById(R.id.tvCustomerSpending);
        }
    }
}

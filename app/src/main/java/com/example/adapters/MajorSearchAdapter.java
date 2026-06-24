package com.example.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411teapp.R;
import com.example.utils.VectorSearchEngine;

import java.util.List;

public class MajorSearchAdapter extends ArrayAdapter<VectorSearchEngine.SearchResult> {
    private Activity context;

    public MajorSearchAdapter(@NonNull Activity context, @NonNull List<VectorSearchEngine.SearchResult> results) {
        super(context, R.layout.item_major_result, results);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.item_major_result, null);
        }

        VectorSearchEngine.SearchResult result = getItem(position);
        if (result == null) return convertView;

        TextView tvMajorName = convertView.findViewById(R.id.tvMajorName);
        TextView tvDepartment = convertView.findViewById(R.id.tvDepartment);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvMatchScore = convertView.findViewById(R.id.tvMatchScore);
        TextView tvMethod = convertView.findViewById(R.id.tvMethod);
        ProgressBar pbMatch = convertView.findViewById(R.id.pbMatch);

        tvMajorName.setText(result.getMajor().getName());
        tvDepartment.setText(result.getMajor().getDepartment());
        tvDescription.setText(result.getMajor().getDescription());

        int pct = result.getMatchPercent();
        tvMatchScore.setText(pct + "%");
        pbMatch.setProgress(pct);

        // Color score badge by match level
        String method = result.getMethod();
        tvMethod.setText("cosine".equals(method) ? "TF-IDF Cosine" : "Levenshtein");

        // Mở URL khi bấm vào card
        convertView.setOnClickListener(v -> {
            String url = result.getMajor().getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        });

        return convertView;
    }
}

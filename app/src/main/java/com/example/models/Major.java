package com.example.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Model đại diện cho một ngành học tại UEL.
 * Chứa tên, mô tả, URL và TF-IDF vector để tính toán độ tương đồng.
 */
public class Major {
    private String name;
    private String description;
    private String url;
    private String department;
    private String[] keywords;
    private Map<String, Double> tfVector; // TF vector cho cosine similarity

    public Major(String name, String description, String department, String url, String[] keywords) {
        this.name = name;
        this.description = description;
        this.department = department;
        this.url = url;
        this.keywords = keywords;
        this.tfVector = new HashMap<>();
        buildTFVector();
    }

    /**
     * Xây dựng TF (Term Frequency) vector từ tên và keywords của ngành.
     * TF(t,d) = số lần xuất hiện của từ t trong document d / tổng số từ trong d
     */
    private void buildTFVector() {
        // Ghép tên và keywords thành một "document"
        StringBuilder doc = new StringBuilder(name.toLowerCase());
        for (String kw : keywords) {
            doc.append(" ").append(kw.toLowerCase());
        }
        doc.append(" ").append(description.toLowerCase());

        String[] terms = doc.toString().split("\\s+");
        Map<String, Integer> termCount = new HashMap<>();
        for (String term : terms) {
            termCount.put(term, termCount.getOrDefault(term, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : termCount.entrySet()) {
            tfVector.put(entry.getKey(), (double) entry.getValue() / terms.length);
        }
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getDepartment() { return department; }
    public String[] getKeywords() { return keywords; }
    public Map<String, Double> getTfVector() { return tfVector; }
}

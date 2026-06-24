package com.example.utils;

import com.example.models.Major;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Engine tìm kiếm ngữ nghĩa dựa trên:
 * 1. TF-IDF Vector + Cosine Similarity
 * 2. Levenshtein Distance cho fallback khi cosine = 0
 */
public class VectorSearchEngine {

    private List<Major> corpus;
    private Map<String, Double> idfMap; // IDF (Inverse Document Frequency) cho toàn bộ corpus

    public VectorSearchEngine(List<Major> corpus) {
        this.corpus = corpus;
        buildIDF();
    }

    /**
     * Tính IDF: IDF(t) = log(N / df(t)) + 1
     * N = tổng số document, df(t) = số document có chứa term t
     */
    private void buildIDF() {
        idfMap = new HashMap<>();
        int N = corpus.size();
        Map<String, Integer> df = new HashMap<>();
        for (Major major : corpus) {
            for (String term : major.getTfVector().keySet()) {
                df.put(term, df.getOrDefault(term, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : df.entrySet()) {
            double idf = Math.log((double) N / entry.getValue()) + 1.0;
            idfMap.put(entry.getKey(), idf);
        }
    }

    /**
     * Tính TF-IDF vector cho một document.
     */
    private Map<String, Double> getTFIDFVector(Map<String, Double> tfVector) {
        Map<String, Double> tfidf = new HashMap<>();
        for (Map.Entry<String, Double> entry : tfVector.entrySet()) {
            String term = entry.getKey();
            double tf = entry.getValue();
            double idf = idfMap.getOrDefault(term, 1.0);
            tfidf.put(term, tf * idf);
        }
        return tfidf;
    }

    /**
     * Tính TF vector cho chuỗi query.
     */
    private Map<String, Double> getQueryTFVector(String query) {
        String[] terms = query.toLowerCase().split("\\s+");
        Map<String, Integer> termCount = new HashMap<>();
        for (String term : terms) {
            termCount.put(term, termCount.getOrDefault(term, 0) + 1);
        }
        Map<String, Double> tf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : termCount.entrySet()) {
            tf.put(entry.getKey(), (double) entry.getValue() / terms.length);
        }
        return tf;
    }

    /**
     * Cosine Similarity = (A · B) / (|A| * |B|)
     * Giá trị trong [0, 1]. Càng gần 1 thì càng giống nhau.
     */
    private double cosineSimilarity(Map<String, Double> vecA, Map<String, Double> vecB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (Map.Entry<String, Double> entry : vecA.entrySet()) {
            String term = entry.getKey();
            double valA = entry.getValue();
            double valB = vecB.getOrDefault(term, 0.0);
            dotProduct += valA * valB;
            normA += valA * valA;
        }
        for (double val : vecB.values()) {
            normB += val * val;
        }

        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * Khoảng cách Levenshtein (Edit Distance) — dùng làm fallback.
     * Đo số thao tác tối thiểu (insert/delete/replace) để biến chuỗi a thành b.
     */
    private int levenshteinDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    /**
     * Phương thức tìm kiếm chính:
     * 1. Tính TF-IDF của query
     * 2. Tính Cosine Similarity với từng ngành
     * 3. Sắp xếp theo điểm giảm dần
     * 4. Nếu tất cả cosine = 0 thì dùng Levenshtein fallback
     *
     * @return danh sách kết quả SearchResult đã sắp xếp theo điểm
     */
    public List<SearchResult> search(String query, int topK) {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();

        Map<String, Double> queryTF = getQueryTFVector(query.trim());
        Map<String, Double> queryTFIDF = new HashMap<>();
        for (Map.Entry<String, Double> e : queryTF.entrySet()) {
            double idf = idfMap.getOrDefault(e.getKey(), Math.log(corpus.size()) + 1.0);
            queryTFIDF.put(e.getKey(), e.getValue() * idf);
        }

        List<SearchResult> results = new ArrayList<>();
        boolean allZero = true;

        for (Major major : corpus) {
            Map<String, Double> docTFIDF = getTFIDFVector(major.getTfVector());
            double score = cosineSimilarity(queryTFIDF, docTFIDF);
            if (score > 0) allZero = false;
            results.add(new SearchResult(major, score, "cosine"));
        }

        // Fallback Levenshtein nếu tất cả cosine = 0
        if (allZero) {
            results.clear();
            String queryLower = query.toLowerCase();
            for (Major major : corpus) {
                int dist = levenshteinDistance(queryLower, major.getName().toLowerCase());
                // Chuẩn hóa về [0,1] — khoảng cách càng nhỏ thì score càng cao
                int maxLen = Math.max(queryLower.length(), major.getName().length());
                double score = 1.0 - (double) dist / maxLen;
                results.add(new SearchResult(major, score, "levenshtein"));
            }
        }

        // Sắp xếp giảm dần theo điểm
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results.subList(0, Math.min(topK, results.size()));
    }

    /**
     * Wrapper class chứa kết quả tìm kiếm kèm điểm số.
     */
    public static class SearchResult {
        private Major major;
        private double score;
        private String method;

        public SearchResult(Major major, double score, String method) {
            this.major = major;
            this.score = score;
            this.method = method;
        }

        public Major getMajor() { return major; }
        public double getScore() { return score; }
        public String getMethod() { return method; }

        /** Phần trăm độ khớp */
        public int getMatchPercent() {
            return (int) Math.round(score * 100);
        }
    }
}

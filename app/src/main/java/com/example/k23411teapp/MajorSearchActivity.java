package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapters.MajorSearchAdapter;
import com.example.models.Major;
import com.example.utils.MajorDataSource;
import com.example.utils.VectorSearchEngine;

import java.util.ArrayList;
import java.util.List;

public class MajorSearchActivity extends AppCompatActivity {

    // UI components
    EditText etQuery;
    LinearLayout btnVoice, btnSearch;
    ListView lvResults;
    ProgressBar progressSearch;
    LinearLayout layoutEmpty;
    TextView tvResultLabel, tvResultCount, tvVoiceHint;

    // Data & Engine
    VectorSearchEngine searchEngine;
    List<VectorSearchEngine.SearchResult> resultList = new ArrayList<>();
    MajorSearchAdapter adapter;

    // Voice input launcher — phải khai báo và khởi tạo TRƯỚC onCreate finish
    ActivityResultLauncher<Intent> voiceLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bước 1: Đăng ký voice launcher TRƯỚC setContentView
        setupVoiceLauncher();

        setContentView(R.layout.activity_major_search);

        // Bước 2: Khởi tạo engine, bind views, setup sự kiện
        initEngine();
        bindViews();
        setupEvents();
    }

    /**
     * Khởi tạo VectorSearchEngine với corpus ngành học UEL.
     */
    private void initEngine() {
        List<Major> majors = MajorDataSource.getMajors();
        searchEngine = new VectorSearchEngine(majors);
    }

    private void bindViews() {
        etQuery = findViewById(R.id.etQuery);
        btnVoice = findViewById(R.id.btnVoice);
        btnSearch = findViewById(R.id.btnSearch);
        lvResults = findViewById(R.id.lvResults);
        progressSearch = findViewById(R.id.progressSearch);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvResultLabel = findViewById(R.id.tvResultLabel);
        tvResultCount = findViewById(R.id.tvResultCount);
        tvVoiceHint = findViewById(R.id.tvVoiceHint);

        adapter = new MajorSearchAdapter(this, resultList);
        lvResults.setAdapter(adapter);
    }

    /**
     * Cấu hình ActivityResultLauncher để nhận kết quả từ Voice Recognition Intent.
     * Phải gọi trong/trước onCreate để tránh crash IllegalStateException.
     */
    private void setupVoiceLauncher() {
        voiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    // tvVoiceHint có thể null nếu được gọi trước bindViews — check an toàn
                    if (tvVoiceHint == null) return;
                    resetVoiceButton();
                    if (activityResult.getResultCode() == RESULT_OK
                            && activityResult.getData() != null) {
                        List<String> results = activityResult.getData()
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (results != null && !results.isEmpty()) {
                            String spoken = results.get(0);
                            etQuery.setText(spoken);
                            etQuery.setSelection(spoken.length());
                            tvVoiceHint.setText("✅ Đã nhận: \"" + spoken + "\"");
                            tvVoiceHint.setTextColor(0xFF43A047);
                            // Tự động tìm kiếm sau khi nhận giọng
                            performSearch(spoken);
                        }
                    } else {
                        resetVoiceButton();
                    }
                }
        );
    }

    private void setupEvents() {
        // Voice button click
        btnVoice.setOnClickListener(v -> {
            animatePulse(v);
            startVoiceRecognition();
        });

        // Search button click
        btnSearch.setOnClickListener(v -> {
            String query = etQuery.getText().toString().trim();
            if (query.isEmpty()) {
                etQuery.setError("Vui lòng nhập từ khóa");
                return;
            }
            hideKeyboard();
            performSearch(query);
        });

        // Enter key trên bàn phím → tìm kiếm
        etQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                btnSearch.performClick();
                return true;
            }
            return false;
        });
    }

    /**
     * Khởi động Google Voice Recognition Intent (tiếng Việt).
     */
    private void startVoiceRecognition() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Thiết bị không hỗ trợ nhận diện giọng nói", Toast.LENGTH_SHORT).show();
            return;
        }

        tvVoiceHint.setText("🔴 Đang nghe...");
        tvVoiceHint.setTextColor(0xFFE53935);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hãy nói tên ngành hoặc từ khóa tìm kiếm...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            resetVoiceButton();
            Toast.makeText(this,
                    "Không thể mở nhận diện giọng nói: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void resetVoiceButton() {
        if (tvVoiceHint != null) {
            tvVoiceHint.setText("🎙 Nhấn micro để tìm bằng giọng nói");
            tvVoiceHint.setTextColor(0xFF3A5269);
        }
    }

    /**
     * Thực hiện tìm kiếm bằng VectorSearchEngine và cập nhật giao diện.
     */
    private void performSearch(String query) {
        // Hiển thị loading
        progressSearch.setVisibility(View.VISIBLE);
        lvResults.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);

        // Chạy search trong background thread
        new Thread(() -> {
            List<VectorSearchEngine.SearchResult> results = searchEngine.search(query, 8);

            runOnUiThread(() -> {
                progressSearch.setVisibility(View.GONE);
                resultList.clear();
                resultList.addAll(results);
                adapter.notifyDataSetChanged();

                if (results.isEmpty()) {
                    layoutEmpty.setVisibility(View.VISIBLE);
                    tvResultLabel.setText("Không có kết quả phù hợp");
                    tvResultCount.setText("");
                } else {
                    lvResults.setVisibility(View.VISIBLE);
                    tvResultLabel.setText("Kết quả cho \"" + query + "\"");
                    tvResultCount.setText(results.size() + " ngành");
                    animateFadeIn(lvResults);
                }
            });
        }).start();
    }

    // ============ Helpers ============

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etQuery.getWindowToken(), 0);
        }
    }

    private void animatePulse(View v) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.85f, 1f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(120);
        scale.setRepeatCount(1);
        scale.setRepeatMode(Animation.REVERSE);
        v.startAnimation(scale);
    }

    private void animateFadeIn(View v) {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(300);
        v.startAnimation(anim);
    }
}

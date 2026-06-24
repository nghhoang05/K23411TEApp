package com.example.k23411teapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adapters.ContactAdapter;
import com.example.database.ContactDatabaseHelper;
import com.example.models.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * FirebaseContactActivity — Hiển thị danh sách liên hệ từ Firebase Realtime Database.
 *
 * Chiến lược dữ liệu:
 *   - Có internet  → addValueEventListener() đến node "contacts" trên Firebase,
 *                     đồng thời cache dữ liệu vào SQLite local.
 *   - Không mạng   → đọc từ SQLite local đã cache trước đó.
 */
public class FirebaseContactActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseContact";
    private static final String NODE = "contacts";

    // ⚠️ Thay URL này bằng URL đúng từ Firebase Console → Realtime Database
    // Vào Firebase Console → Project → Realtime Database → URL ở đầu trang
    // Thường có dạng: https://<project-id>-default-rtdb.firebaseio.com
    // Hoặc nếu ở region Asia: https://<project-id>-default-rtdb.asia-southeast1.firebasedatabase.app
    private static final String DB_URL = "https://k23411te-default-rtdb.asia-southeast1.firebasedatabase.app";

    // UI
    private ListView lvContact;
    private ProgressBar progressContacts;
    private TextView tvNetworkStatus, tvContactCount, tvDataSource, tvEmptyMessage;
    private View layoutEmpty;

    // Data
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;
    private ContactDatabaseHelper dbHelper;

    // Firebase
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_contact);

        dbHelper = new ContactDatabaseHelper(this);
        bindViews();

        Log.d(TAG, "Network available: " + isNetworkAvailable());

        if (isNetworkAvailable()) {
            showOnlineBadge();
            loadFromFirebase();
        } else {
            showOfflineBadge();
            loadFromLocalDB();
        }
    }

    // ──────────────────────────────────────────────
    //  Setup UI
    // ──────────────────────────────────────────────

    private void bindViews() {
        lvContact        = findViewById(R.id.lvContact);
        progressContacts = findViewById(R.id.progressContacts);
        tvNetworkStatus  = findViewById(R.id.tvNetworkStatus);
        tvContactCount   = findViewById(R.id.tvContactCount);
        tvDataSource     = findViewById(R.id.tvDataSource);
        tvEmptyMessage   = findViewById(R.id.tvEmptyMessage);
        layoutEmpty      = findViewById(R.id.layoutEmpty);

        contactList    = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contactList);
        lvContact.setAdapter(contactAdapter);
    }

    // ──────────────────────────────────────────────
    //  Network check
    // ──────────────────────────────────────────────

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    // ──────────────────────────────────────────────
    //  Badge hiển thị trạng thái
    // ──────────────────────────────────────────────

    private void showOnlineBadge() {
        tvNetworkStatus.setText("🟢 Online — Đang tải từ Firebase...");
        tvNetworkStatus.setTextColor(0xFF43A047);
        tvDataSource.setText("Nguồn: Firebase Realtime DB");
    }

    private void showOfflineBadge() {
        tvNetworkStatus.setText("🔴 Offline — Dữ liệu từ Local DB");
        tvNetworkStatus.setTextColor(0xFFE53935);
        tvDataSource.setText("Nguồn: SQLite (Cache)");
    }

    // ──────────────────────────────────────────────
    //  Load từ Firebase (Online)
    // ──────────────────────────────────────────────

    private void loadFromFirebase() {
        showLoading(true);
        Log.d(TAG, "loadFromFirebase: connecting to node=" + NODE + " url=" + DB_URL);

        // Chỉ định rõ URL để tránh dùng sai database instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(DB_URL);

        // Bật persistence để cache offline tự động (bổ sung thêm lớp bảo vệ)
        try {
            firebaseDatabase.setPersistenceEnabled(true);
        } catch (Exception e) {
            // setPersistenceEnabled chỉ gọi được 1 lần, ignore nếu đã gọi rồi
            Log.w(TAG, "setPersistenceEnabled already called");
        }

        dbRef = firebaseDatabase.getReference(NODE);

        // Giữ dữ liệu đồng bộ kể cả khi không có listener
        dbRef.keepSynced(true);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                showLoading(false);
                Log.d(TAG, "onDataChange: childrenCount=" + snapshot.getChildrenCount());

                if (!snapshot.exists()) {
                    Log.w(TAG, "onDataChange: snapshot rỗng — kiểm tra node 'contacts' trên Firebase");
                    tvNetworkStatus.setText("⚠️ Node 'contacts' chưa có dữ liệu");
                    tvNetworkStatus.setTextColor(0xFFFF8F00);
                    showEmptyState(true);
                    tvEmptyMessage.setText("Node 'contacts' trống\nHãy thêm dữ liệu vào Firebase Console");
                    return;
                }

                // Xóa cache cũ để sync mới
                dbHelper.clearAll();
                List<Contact> freshList = new ArrayList<>();

                // Parse DataSnapshot
                for (DataSnapshot child : snapshot.getChildren()) {
                    String key   = child.getKey();
                    String name  = child.child("name").getValue(String.class);
                    String email = child.child("email").getValue(String.class);
                    Object phoneObj = child.child("phone").getValue();
                    String phone = phoneObj != null ? phoneObj.toString() : "";

                    Log.d(TAG, "Parsed contact: key=" + key + " name=" + name
                            + " email=" + email + " phone=" + phone);

                    Contact contact = new Contact(key, name, email, phone);
                    freshList.add(contact);

                    // Cache vào SQLite
                    dbHelper.insertOrReplace(contact);
                }

                // Cập nhật UI
                updateList(freshList);
                tvNetworkStatus.setText("🟢 Online — Firebase (" + freshList.size() + " liên hệ)");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showLoading(false);
                Log.e(TAG, "onCancelled: code=" + error.getCode()
                        + " msg=" + error.getMessage()
                        + " details=" + error.getDetails());

                // Hiển thị lỗi cụ thể
                String errMsg = "Lỗi " + error.getCode() + ": " + error.getMessage();
                Toast.makeText(FirebaseContactActivity.this, errMsg, Toast.LENGTH_LONG).show();

                tvNetworkStatus.setText("⚠️ Lỗi Firebase — Dùng Local DB");
                tvNetworkStatus.setTextColor(0xFFFF8F00);
                tvDataSource.setText(errMsg);

                // Fallback sang local cache
                loadFromLocalDB();
            }
        };

        dbRef.addValueEventListener(valueEventListener);
    }

    // ──────────────────────────────────────────────
    //  Load từ SQLite local (Offline)
    // ──────────────────────────────────────────────

    private void loadFromLocalDB() {
        showLoading(true);
        List<Contact> cached = dbHelper.getAllContacts();
        showLoading(false);
        Log.d(TAG, "loadFromLocalDB: count=" + cached.size());

        if (cached.isEmpty()) {
            tvEmptyMessage.setText("Chưa có dữ liệu cache.\nVui lòng kết nối internet lần đầu.");
            showEmptyState(true);
        } else {
            updateList(cached);
        }
    }

    // ──────────────────────────────────────────────
    //  Cập nhật ListView
    // ──────────────────────────────────────────────

    private void updateList(List<Contact> list) {
        contactList.clear();
        contactList.addAll(list);
        contactAdapter.notifyDataSetChanged();

        if (list.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
            lvContact.setVisibility(View.VISIBLE);
            tvContactCount.setText(list.size() + " liên hệ");
        }
    }

    // ──────────────────────────────────────────────
    //  Helpers hiển thị / ẩn UI states
    // ──────────────────────────────────────────────

    private void showLoading(boolean show) {
        progressContacts.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) lvContact.setVisibility(View.GONE);
    }

    private void showEmptyState(boolean show) {
        layoutEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        lvContact.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // ──────────────────────────────────────────────
    //  Lifecycle — tránh memory leak
    // ──────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbRef != null && valueEventListener != null) {
            dbRef.removeEventListener(valueEventListener);
        }
    }
}

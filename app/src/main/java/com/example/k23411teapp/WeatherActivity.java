package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adapters.ProvinceAdapter;
import com.example.models.Province;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherActivity extends AppCompatActivity {
    ListView lvProvinces;
    ProgressBar progressBar;
    ArrayList<Province> provinceList;
    ProvinceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        addViews();
        addEvents();
        fetchProvinces();
    }

    private void addViews() {
        lvProvinces = findViewById(R.id.lvProvinces);
        progressBar = findViewById(R.id.progressBar);
        provinceList = new ArrayList<>();
        adapter = new ProvinceAdapter(this, R.layout.item_province, provinceList);
        lvProvinces.setAdapter(adapter);
    }

    private void addEvents() {
        lvProvinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province selected = provinceList.get(position);
                Intent intent = new Intent(WeatherActivity.this, WeatherDetailActivity.class);
                intent.putExtra("PROVINCE", selected);
                startActivity(intent);
            }
        });
    }

    private void fetchProvinces() {
        progressBar.setVisibility(View.VISIBLE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://thanhnien.vn/ajax-get-item-weather-htm");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    connection.disconnect();

                    String html = sb.toString();
                    ArrayList<Province> tempList = new ArrayList<>();
                    
                    // Regex to parse: <option value="20070076" >Kon Tum</option>
                    Pattern pattern = Pattern.compile("<option[^>]*value=\"(\\d+)\"[^>]*>([^<]+)</option>");
                    Matcher matcher = pattern.matcher(html);
                    while (matcher.find()) {
                        String id = matcher.group(1);
                        String name = matcher.group(2).trim();
                        tempList.add(new Province(id, name));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (tempList.isEmpty()) {
                                Toast.makeText(WeatherActivity.this, "Không thể lấy danh sách tỉnh thành", Toast.LENGTH_SHORT).show();
                            } else {
                                provinceList.clear();
                                provinceList.addAll(tempList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(WeatherActivity.this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

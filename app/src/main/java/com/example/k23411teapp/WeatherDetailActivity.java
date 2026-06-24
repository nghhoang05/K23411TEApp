package com.example.k23411teapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Province;
import com.example.models.WeatherInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherDetailActivity extends AppCompatActivity {
    TextView tvLocation, tvTemperature, tvStatus, tvHumidity, tvWind;
    ProgressBar progressBarDetail;
    Province province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        addViews();
        getIntentData();
        if (province != null) {
            fetchWeather(province.getId());
        }
    }

    private void addViews() {
        tvLocation = findViewById(R.id.tvLocation);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvStatus = findViewById(R.id.tvStatus);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        progressBarDetail = findViewById(R.id.progressBarDetail);
    }

    private void getIntentData() {
        if (getIntent().hasExtra("PROVINCE")) {
            province = (Province) getIntent().getSerializableExtra("PROVINCE");
            tvLocation.setText(province.getName());
        }
    }

    private void fetchWeather(String id) {
        progressBarDetail.setVisibility(View.VISIBLE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://eth2.cnnd.vn/ajax/weatherinfo/" + id + ".htm");
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

                    String jsonStr = sb.toString();
                    JSONObject root = new JSONObject(jsonStr);
                    JSONObject dataOuter = root.getJSONObject("Data");
                    JSONObject dataInner = dataOuter.getJSONObject("data");
                    JSONObject dataInfo = dataInner.getJSONObject("datainfo");

                    String location = dataInfo.getString("location");
                    int temperature = dataInfo.getInt("temperature");
                    String status = dataInfo.getString("status");
                    String humidity = dataInfo.getString("humidity");
                    
                    JSONObject windObj = dataInfo.getJSONObject("wind");
                    String wind = windObj.getString("index") + " " + windObj.getString("unit");

                    WeatherInfo weatherInfo = new WeatherInfo(location, temperature, status, humidity, wind);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarDetail.setVisibility(View.GONE);
                            updateUI(weatherInfo);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarDetail.setVisibility(View.GONE);
                            Toast.makeText(WeatherDetailActivity.this, "Lỗi lấy thời tiết: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void updateUI(WeatherInfo info) {
        tvLocation.setText(info.getLocation());
        tvTemperature.setText(info.getTemperature() + "°");
        tvStatus.setText(info.getStatus());
        tvHumidity.setText(info.getHumidity());
        tvWind.setText(info.getWind());
    }
}

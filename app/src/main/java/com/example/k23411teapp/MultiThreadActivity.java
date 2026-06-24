package com.example.k23411teapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MultiThreadActivity extends AppCompatActivity {
    EditText edtNumberButton;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    LinearLayout linearLayoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_thread);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtNumberButton = findViewById(R.id.edtNumberButton);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarPercent = findViewById(R.id.progressBarPercent);
        linearLayoutButton = findViewById(R.id.linearLayoutButton);
    }
    Handler mainThread=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int value=msg.arg1;
            int percent=msg.arg2;
            txtPercent.setText(percent+"%");
            progressBarPercent.setProgress(percent);

            Button button=new Button(MultiThreadActivity.this);
            button.setWidth(300);
            button.setHeight(50);
            button.setText(value+"");
            linearLayoutButton.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b=(Button) v;
                    b.setTextColor(Color.RED);
                }
            });
            return false;
        }
    });

    public void processMultiThreading(View view) {
        int n=Integer.parseInt(edtNumberButton.getText().toString());
        //Khai báo tiểu trình (đa tiến trình chạy background longtime)
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                //Xử lý longtime task ở đây
                //Trong này không được truy suất tới bất kỳ biến Views (GUI) nào
                //Nó phải gửi thông điệp về cho MainThread xử lý Visualization
                Random random=new Random();
                for(int i=1;i<=n;i++)
                {
                    int value=random.nextInt(100);
                    int percent=i*100/n;
                    //Lấy Message từ MainThread:
                    Message message=mainThread.obtainMessage();
                    //Gửi giá trị mới cho message
                    message.arg1=value; //giả sử lưu giá trị vào arg1
                    message.arg2=percent; //giả sử lưu tỉ lệ thực hiện vào arg2
                    //Gửi message tới MainThread
                    mainThread.sendMessage(message);
                    //Cần tạm thời sleep 1 khoảng thời gian để các tiến trình khác có thể thực hiện
                    try {
                        Thread.sleep(100);
                        } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Kích hoạt tiểu trình
        th.start();
    }
}
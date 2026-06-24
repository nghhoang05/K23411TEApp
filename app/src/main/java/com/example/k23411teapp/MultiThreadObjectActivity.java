package com.example.k23411teapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.DataWareHouse;
import com.example.models.Product;

import java.util.ArrayList;

public class MultiThreadObjectActivity extends AppCompatActivity {
    EditText edtNumberProduct;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    ListView lvProduct;
    Button btnDownload;
    ArrayList<Product> products;
    ArrayAdapter<Product>adapterProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_thread_object);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void addEvents() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDownloadProduct();
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0 && position<products.size())
                {
                    products.remove(position);
                    adapterProduct.notifyDataSetChanged();
                }
                return false;
            }
        });
    }
    //Main Thread
    Handler mainThread=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int percent=msg.arg1;
            txtPercent.setText(percent+"%");
            progressBarPercent.setProgress(percent);
            if(msg.obj!=null)
            {
                Product product=(Product) msg.obj;
                products.add(product);
                adapterProduct.notifyDataSetChanged();
            }
            if(percent==100)
            {
                Toast.makeText(MultiThreadObjectActivity.this, "Download complete", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    private void processDownloadProduct() {
        int n = Integer.parseInt(edtNumberProduct.getText().toString());
        //Tiểu trình
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    Product product= DataWareHouse.downloadProduct(i);
                    int percent=(i+1)*100/n;
                    //Lấy message từ MainThread
                    Message msg=mainThread.obtainMessage();
                    //Gán các message cho MainThread
                    msg.obj=product;
                    msg.arg1=percent;
                    //Gửi lại message cho MainThread
                    mainThread.sendMessage(msg);
                    //Cần tạm dùng tiểu trình để các tiến trình khác có thể thực hiện
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                Message finalMessage=mainThread.obtainMessage();
                finalMessage.arg1=100;
                mainThread.sendMessage(finalMessage);
            }
        });
        thread.start(); //kích hoạt tiểu trình để chạy longtime task
    }

    private void addViews() {
        edtNumberProduct = findViewById(R.id.edtNumberProduct);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarPercent = findViewById(R.id.progressBarPercent);
        lvProduct = findViewById(R.id.lvProduct);
        btnDownload = findViewById(R.id.btnDownload);
        products = new ArrayList<>();
        adapterProduct = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProduct.setAdapter(adapterProduct);
    }
    }

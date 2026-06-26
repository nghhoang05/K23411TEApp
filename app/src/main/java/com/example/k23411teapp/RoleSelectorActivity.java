package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RoleSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selector);

        CardView cardAdmin  = findViewById(R.id.cardAdmin);
        CardView cardClient = findViewById(R.id.cardClient);

        cardAdmin.setOnClickListener(v ->
                startActivity(new Intent(this, AdminDashboardActivity.class)));

        cardClient.setOnClickListener(v ->
                startActivity(new Intent(this, ClientHomeActivity.class)));
    }
}

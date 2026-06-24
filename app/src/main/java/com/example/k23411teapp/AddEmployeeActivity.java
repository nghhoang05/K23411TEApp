package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Employee;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEmployeeActivity extends AppCompatActivity {

    TextInputEditText edtId, edtName, edtPhone;
    AutoCompleteTextView actBirthPlace;
    String[] listOfBirthPlace;
    ArrayAdapter<String> adapterBirthPlace;
    ExtendedFloatingActionButton imgAddEmployee;
    ImageView imgEditEmployee, imgDeleteEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        imgAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSaveEmployee();
            }
        });
    }

    private void processSaveEmployee() {
        Employee employee = new Employee();
        employee.setId(edtId.getText().toString());
        employee.setName(edtName.getText().toString());
        employee.setPhone(edtPhone.getText().toString());
        employee.setBirthplace(actBirthPlace.getText().toString());

        Intent intent = getIntent();
        intent.putExtra("EMPLOYEE_K23411TE", employee);
        setResult(888, intent);
        finish();
    }

    private void addViews() {
        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        actBirthPlace = findViewById(R.id.actBirthPlace);

        listOfBirthPlace = getResources().getStringArray(R.array.arr_province);

        adapterBirthPlace = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfBirthPlace);
        actBirthPlace.setAdapter(adapterBirthPlace);

        imgAddEmployee = findViewById(R.id.imgAddEmployee);
        imgEditEmployee = findViewById(R.id.imgEditEmployee);
        imgDeleteEmployee = findViewById(R.id.imgDeleteEmployee);
    }
}

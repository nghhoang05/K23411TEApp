package com.example.k23411teapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.ExpressionBuilder;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {

    EditText edtFormular;
    Button btnDel, btnC, btnCE, btnPercent, btn1_x, btnX2, btnSqrt, btnSign;
    Button btnCalculate;
    TextView txtMC, txtMR, txtMplus, txtMminus, txtMS, txtM;
    View.OnClickListener m_click_listener;
    double memoryValue = 0;
    private static final String PREFS_NAME = "CalculatorPrefs";
    private static final String KEY_FORMULA = "lastFormula";
    private static final String KEY_MEMORY = "memoryValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        addView();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnDel.setOnClickListener(v -> {
            String formular = edtFormular.getText().toString();
            if (formular.length() > 0) {
                edtFormular.setText(formular.substring(0, formular.length() - 1));
            }
        });

        btnCalculate.setOnClickListener(v -> calculateResult());

        btnC.setOnClickListener(v -> edtFormular.setText(""));
        btnCE.setOnClickListener(v -> edtFormular.setText(""));

        btnPercent.setOnClickListener(v -> applyFunction("(%s)/100"));
        btn1_x.setOnClickListener(v -> applyFunction("1/(%s)"));
        btnX2.setOnClickListener(v -> applyFunction("(%s)^2"));
        btnSqrt.setOnClickListener(v -> applyFunction("sqrt(%s)"));
        btnSign.setOnClickListener(v -> {
            String val = edtFormular.getText().toString();
            if (val.startsWith("-")) {
                edtFormular.setText(val.substring(1));
            } else if (!val.isEmpty()) {
                edtFormular.setText("-" + val);
            }
        });

        m_click_listener = view -> {
            int id = view.getId();
            if (id == R.id.txtM) {
                Toast.makeText(this, "Memory: " + memoryValue, Toast.LENGTH_SHORT).show();
            } else if (id == R.id.txtMC) {
                memoryValue = 0;
                Toast.makeText(this, "Memory Cleared", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.txtMR) {
                edtFormular.append(String.valueOf(memoryValue));
            } else if (id == R.id.txtMS) {
                try {
                    memoryValue = Double.parseDouble(edtFormular.getText().toString());
                    Toast.makeText(this, "Memory Stored", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid number to store", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.txtMplus) {
                try {
                    memoryValue += Double.parseDouble(edtFormular.getText().toString());
                } catch (Exception e) {}
            } else if (id == R.id.txtMminus) {
                try {
                    memoryValue -= Double.parseDouble(edtFormular.getText().toString());
                } catch (Exception e) {}
            }
        };

        txtMC.setOnClickListener(m_click_listener);
        txtMR.setOnClickListener(m_click_listener);
        txtMplus.setOnClickListener(m_click_listener);
        txtMminus.setOnClickListener(m_click_listener);
        txtMS.setOnClickListener(m_click_listener);
        txtM.setOnClickListener(m_click_listener);
    }

    private void calculateResult() {
        try {
            String formula = edtFormular.getText().toString()
                    .replace("÷", "/")
                    .replace("×", "*")
                    .replace("−", "-")
                    .replace(":", "/");
            if (formula.isEmpty()) return;
            double result = new ExpressionBuilder(formula).build().evaluate();
            // Format result to remove .0 if it's an integer
            if (result == (long) result) {
                edtFormular.setText(String.valueOf((long) result));
            } else {
                edtFormular.setText(String.valueOf(result));
            }
        } catch (ArithmeticException e) {
            Toast.makeText(this, "Division by zero!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Syntax Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyFunction(String pattern) {
        String current = edtFormular.getText().toString();
        if (!current.isEmpty()) {
            // Use String.format for better readability, e.g., "(%s)/100"
            edtFormular.setText(String.format(pattern, current));
            calculateResult();
        }
    }

    private void addView() {
        edtFormular = findViewById(R.id.edtFormular);
        btnDel = findViewById(R.id.btnDel);
        btnC = findViewById(R.id.btnC);
        btnCE = findViewById(R.id.btnCE);
        btnPercent = findViewById(R.id.btnPercent);
        btn1_x = findViewById(R.id.btn1_x);
        btnX2 = findViewById(R.id.x_2);
        btnSqrt = findViewById(R.id.btnsqrt);
        btnSign = findViewById(R.id.btnsign);
        btnCalculate = findViewById(R.id.btnCalculate);

        txtMC = findViewById(R.id.txtMC);
        txtMR = findViewById(R.id.txtMR);
        txtMplus = findViewById(R.id.txtMplus);
        txtMminus = findViewById(R.id.txtMminus);
        txtMS = findViewById(R.id.txtMS);
        txtM = findViewById(R.id.txtM);
    }

    public void processInputData(View view) {
        Button btn = (Button) view;
        edtFormular.append(btn.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FORMULA, edtFormular.getText().toString());
        editor.putLong(KEY_MEMORY, Double.doubleToRawLongBits(memoryValue));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastFormula = prefs.getString(KEY_FORMULA, "");
        edtFormular.setText(lastFormula);
        edtFormular.setSelection(edtFormular.getText().length());

        long memoryLong = prefs.getLong(KEY_MEMORY, Double.doubleToRawLongBits(0));
        memoryValue = Double.longBitsToDouble(memoryLong);
    }
}
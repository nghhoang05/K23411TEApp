package com.example.k23411teapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class EmployeeManagementActivity extends AppCompatActivity {

    EditText edtId,edtName,edtPhone;
    ListView lvEmployee;
    Button btnSave,btnDelete,btnExit;

    ArrayList<String>listOfEmployee;
    ArrayAdapter<String>adapterEmployee;
    int selectedPosition = -1;
    String name_prefs = "EmployeePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_management);
        addViews();
        sampleData();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
                adapterEmployee.notifyDataSetChanged();
                String emp=listOfEmployee.get(i);
                String []arrInfor=emp.split("-");
                if(arrInfor.length==3)
                {
                    edtId.setText(arrInfor[0]);
                    edtName.setText(arrInfor[1]);
                    edtPhone.setText(arrInfor[2]);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(name_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("SELECTED_INDEX", selectedPosition);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(name_prefs, MODE_PRIVATE);
        selectedPosition = preferences.getInt("SELECTED_INDEX", -1);
        if (selectedPosition != -1 && selectedPosition < listOfEmployee.size()) {
            String emp = listOfEmployee.get(selectedPosition);
            String[] arrInfor = emp.split("-");
            if (arrInfor.length == 3) {
                edtId.setText(arrInfor[0]);
                edtName.setText(arrInfor[1]);
                edtPhone.setText(arrInfor[2]);
            }
            lvEmployee.setSelection(selectedPosition);
            adapterEmployee.notifyDataSetChanged();
        }
    }

    private void sampleData() {
        listOfEmployee.add("e1-Nguyen Van A-0123456789");
        listOfEmployee.add("e2-Nguyen Van B-0123456789");
        listOfEmployee.add("e3-Nguyen Van C-0123456789");
        //hoặc tạo vòn lặp thêm khoản 1000 nhân viên
        Random random=new Random(123); // Seed for consistency so index matches person after restart
        for(int i=0;i<10;i++)
        {
            String id="e"+(i+1);
            String name="Name "+i;
            String phone="090";
            int provider=random.nextInt(3);
            if (provider==1)
                phone="098";
            else if(provider==2)
                phone="094";
            for(int p=1;p<7;p++)
                phone+=random.nextInt(10);
            listOfEmployee.add(id+"-"+name+"-"+phone);
        }
        adapterEmployee.notifyDataSetChanged();
    }

    private void addViews() {
        edtId=findViewById(R.id.edtId);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        lvEmployee=findViewById(R.id.lvEmployee);
        btnSave=findViewById(R.id.btnSave);
        btnDelete=findViewById(R.id.btnDelete);
        btnExit=findViewById(R.id.btn_Exit);
        listOfEmployee=new ArrayList<>();
        adapterEmployee=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfEmployee) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == selectedPosition) {
                    v.setBackgroundColor(Color.YELLOW);
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                return v;
            }
        };
        lvEmployee.setAdapter(adapterEmployee);
    }

    public void closeActivity(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        ImageView imgYes=dialog.findViewById(R.id.imgYes);
        ImageView imgCancel=dialog.findViewById(R.id.imgCancel);
        imgYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void saveEmployee(View view) {
        String id = edtId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (id.isEmpty()) {
            edtId.setError(getString(R.string.str_id_required));
            edtId.requestFocus();
            return;
        }

        String empData = id + "-" + name + "-" + phone;
        int foundIndex = -1;

        // Duyệt danh sách để kiểm tra ID đã tồn tại chưa
        for (int i = 0; i < listOfEmployee.size(); i++) {
            String item = listOfEmployee.get(i);
            String[] parts = item.split("-");
            if (parts.length > 0 && parts[0].equalsIgnoreCase(id)) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) {
            // Nếu đã tồn tại thì cập nhật
            listOfEmployee.set(foundIndex, empData);
            selectedPosition = foundIndex;
            Toast.makeText(this, getString(R.string.str_updated), Toast.LENGTH_SHORT).show();
        } else {
            // Nếu chưa có thì thêm mới
            listOfEmployee.add(empData);
            selectedPosition = listOfEmployee.size() - 1;
            Toast.makeText(this, getString(R.string.str_added), Toast.LENGTH_SHORT).show();
        }

        // Cập nhật giao diện và cuộn tới dòng vừa thao tác
        adapterEmployee.notifyDataSetChanged();
        lvEmployee.setSelection(selectedPosition);
    }

    public void deleteEmployee(View view) {
        if (selectedPosition == -1) {
            Toast.makeText(this, getString(R.string.str_select_to_delete), Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.str_confirm_delete_title));
        builder.setMessage(getString(R.string.str_confirm_delete_message));
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(getString(R.string.str_confirm_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listOfEmployee.remove(selectedPosition);
                selectedPosition = -1; // Reset selection
                adapterEmployee.notifyDataSetChanged();
                
                // Clear inputs
                edtId.setText("");
                edtName.setText("");
                edtPhone.setText("");
                edtId.requestFocus();

                Toast.makeText(EmployeeManagementActivity.this, getString(R.string.str_deleted), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getString(R.string.str_confirm_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
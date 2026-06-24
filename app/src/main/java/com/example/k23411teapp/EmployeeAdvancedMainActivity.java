package com.example.k23411teapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.EmployeeAdapter;
import com.example.models.Department;
import com.example.models.Employee;

import java.util.ArrayList;

public class EmployeeAdvancedMainActivity extends AppCompatActivity {

    ListView lvEmployee;
    ArrayList<Employee> listOfEmployee;
    EmployeeAdapter adapterEmployee;
    Spinner spDepartment;
    ArrayList<Department> listOfDepartment;
    ArrayAdapter<Department> adapterDepartment;
    ImageView imgAddEmployee, imgEditEmployee, imgDeleteEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_advanced_main);
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
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department selectedDepartment = listOfDepartment.get(position);
                adapterEmployee.clear();
                adapterEmployee.addAll(selectedDepartment.getListOfEmployee());
                adapterEmployee.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        imgAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeAdvancedMainActivity.this,AddEmployeeActivity.class);
                startActivityForResult(intent, 888);
            }
        });
    }

    private void sampleData() {
        // Sử dụng tài nguyên chuỗi thay vì hardcode
        Department dAll = new Department("all", getString(R.string.str_all));
        Department d1 = new Department("d1", getString(R.string.str_dept_1));
        Department d2 = new Department("d2", getString(R.string.str_dept_2));
        Department d3 = new Department("d3", getString(R.string.str_dept_3));

        // Tạo dữ liệu nhân viên mẫu (Có thể để trong resource nếu cần, nhưng thường data sẽ từ DB/API)
        Employee e1 = new Employee("e1", "Nguyen Van A", "0123456789");
        Employee e2 = new Employee("e2", "Tran Thi B", "0987654321");
        Employee e3 = new Employee("e3", "Le Van C", "0111222333");
        Employee e4 = new Employee("e4", "Pham Minh D", "0444555666");
        Employee e5 = new Employee("e5", "Hoang Lan E", "0777888999");
        Employee e6 = new Employee("e6", "Doan Van F", "0666777888");

        // Thêm nhân viên vào từng phòng ban cụ thể
        d1.addEmployee(e1);
        d1.addEmployee(e2);
        d2.addEmployee(e3);
        d2.addEmployee(e4);
        d3.addEmployee(e5);
        d3.addEmployee(e6);

        // Thêm TẤT CẢ nhân viên vào nhóm "All"
        dAll.addEmployee(e1);
        dAll.addEmployee(e2);
        dAll.addEmployee(e3);
        dAll.addEmployee(e4);
        dAll.addEmployee(e5);
        dAll.addEmployee(e6);

        // Cập nhật danh sách phòng ban cho Spinner (All nằm đầu tiên)
        listOfDepartment.clear();
        listOfDepartment.add(dAll);
        listOfDepartment.add(d1);
        listOfDepartment.add(d2);
        listOfDepartment.add(d3);

        adapterDepartment.notifyDataSetChanged();
    }

    private void addViews() {
        lvEmployee=findViewById(R.id.lvEmployee);
        listOfEmployee=new ArrayList<>();
        adapterEmployee=new EmployeeAdapter(this,R.layout.item_custom_employee, listOfEmployee);
        lvEmployee.setAdapter(adapterEmployee);
        spDepartment=findViewById(R.id.spDepartment);
        listOfDepartment=new ArrayList<>();
        adapterDepartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfDepartment);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapterDepartment);
        
        imgAddEmployee = findViewById(R.id.imgAddEmployee);
        imgEditEmployee = findViewById(R.id.imgEditEmployee);
        imgDeleteEmployee = findViewById(R.id.imgDeleteEmployee);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 888 && resultCode == 888 && data != null)
        {
            Employee emp = (Employee) data.getSerializableExtra("EMPLOYEE_K23411TE");
            if (emp == null) return;

            int selectedPos = spDepartment.getSelectedItemPosition();
            Department dAll = listOfDepartment.get(0);
            Department dNS = listOfDepartment.get(3); // Phòng nhân sự (Department 3)

            // 1. Luôn thêm vào danh sách "Tất cả"
            dAll.addEmployee(emp);

            // 2. Thêm vào phòng ban tương ứng
            if (selectedPos == 0) {
                // Nếu đang chọn "Tất cả", thêm vào "Phòng nhân sự"
                dNS.addEmployee(emp);
            } else {
                // Nếu đang chọn một phòng ban cụ thể, thêm vào phòng đó
                listOfDepartment.get(selectedPos).addEmployee(emp);
            }

            // 3. Cập nhật lại ListView hiển thị cho phòng ban hiện tại
            Department currentDept = listOfDepartment.get(selectedPos);
            adapterEmployee.clear();
            adapterEmployee.addAll(currentDept.getListOfEmployee());
            adapterEmployee.notifyDataSetChanged();
        }
    }
}
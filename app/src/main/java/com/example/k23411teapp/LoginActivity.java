package com.example.k23411teapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.ListUserAccount;
import com.example.models.UserAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {

    EditText editUserName;
    EditText editPassword;
    TextView txtMessage;

    CheckBox chkSaveInfor;

    String name_share_refs="LoginInfor";

    RadioButton radAdministrator,radEmployee;
    Button btnLogin;
    BroadcastReceiver internetStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if(connectivityManager!=null)
            {
                NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null && networkInfo.isConnected())
                {
                    btnLogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    btnLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        addViews();
        copyDataBase();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        editUserName=findViewById(R.id.editUserName);
        editPassword=findViewById(R.id.editPassword);
        txtMessage=findViewById(R.id.txtMessage);
        chkSaveInfor=findViewById(R.id.chkSaveInfor);
        radAdministrator=findViewById(R.id.radAdministrator);
        radEmployee=findViewById(R.id.radEmployee);
        btnLogin=findViewById(R.id.btnLogin);
    }

    public void loginSystem(View view) {
        String username=editUserName.getText().toString();
        String pwd=editPassword.getText().toString();
        UserAccount acc= ListUserAccount.Login(username,pwd);
        if (acc!=null)
        {
            SharedPreferences preferences=getSharedPreferences(name_share_refs,MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("UserName", username);
            editor.putString("Password", pwd);
            boolean saved=chkSaveInfor.isChecked();
            editor.putBoolean("SAVED", saved);
            editor.apply();

            if(radAdministrator.isChecked())
            {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ACCOUNT",acc);
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(LoginActivity.this, EmployeeAdvancedMainActivity.class);
                startActivity(intent);
            }

            txtMessage.setText(getString(R.string.str_login_successfull));
        }
        else {
            txtMessage.setText(getString(R.string.str_login_failed));
        }
    }
    public void loginSystemOld(View view) {
        String username=editUserName.getText().toString();
        String pwd=editPassword.getText().toString();
        if (username.equalsIgnoreCase("admin") && pwd.equals("123"))
        {
            SharedPreferences preferences=getSharedPreferences(name_share_refs,MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("UserName", username);
            editor.putString("Password", pwd);
            boolean saved=chkSaveInfor.isChecked();
            editor.putBoolean("SAVED", saved);
            editor.apply();

            if(radAdministrator.isChecked())
            {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(LoginActivity.this, EmployeeAdvancedMainActivity.class);
                startActivity(intent);
            }

            txtMessage.setText(getString(R.string.str_login_successfull));
        }
        else
        {
            txtMessage.setText(getString(R.string.str_login_failed));
        }
    }

    public void exitSystem(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);

        ImageView imgYes = customView.findViewById(R.id.imgYes);
        ImageView imgCancel = customView.findViewById(R.id.imgCancel);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set background transparent to show rounded corners from XML
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        imgYes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        imgCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
        @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences(name_share_refs,MODE_PRIVATE);
        String username=preferences.getString("UserName","");
        String password=preferences.getString("Password","");
        boolean saved=preferences.getBoolean("SAVED",false);
        if(saved)
        {
            editUserName.setText(username);
            editPassword.setText(password);
        }
        chkSaveInfor.setChecked(saved);

        IntentFilter internetFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetStateReceiver,internetFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetStateReceiver);
    }

    public static final String DATABASE_NAME = "K23411TEDSales.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    private void copyDataBase(){
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            Log.d("DATABASE_DEBUG", "Checking database at: " + dbFile.getAbsolutePath());
            Log.d("DATABASE_DEBUG", "Database exists: " + dbFile.exists() + ", size: " + dbFile.length());
            
            // Force copy if file is smaller than the asset (asset is ~28KB)
            if(!dbFile.exists() || dbFile.length() < 20000){
                Log.d("DATABASE_DEBUG", "Database missing or too small, copying...");
                if(CopyDBFromAsset()){
                    Toast.makeText(LoginActivity.this,
                            "Copy database successful!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("DATABASE_DEBUG", "Database already exists and size looks okay.");
            }
        }catch (Exception e){
            Log.e("DATABASE_DEBUG", "Error in copyDataBase: " + e.toString());
        }
    }

    private boolean CopyDBFromAsset() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            int size = inputStream.available();
            Log.d("DATABASE_DEBUG", "Asset size: " + size);
            
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush();  outputStream.close(); inputStream.close();
            Log.d("DATABASE_DEBUG", "Database copied to: " + dbPath);
            return  true;
        } catch (IOException e) {
            Log.e("DATABASE_DEBUG", "Copy failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
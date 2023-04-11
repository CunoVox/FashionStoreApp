package com.example.fashionstoreapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.RetrofitService;
import com.example.fashionstoreapp.Retrofit.UserAPI;
import com.example.fashionstoreapp.SharedPrefManager.ObjectSharedPreferences;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword, etUserName;
    Button btnLogin;
    TextView tvRegister;
    User user = new User();

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        btnLoginClick();
        tvRegisterClick();
    }

    private void tvRegisterClick() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void btnLoginClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        etPassword = findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);
        if (TextUtils.isEmpty(etUserName.getText().toString())){
            etUserName.setError("Please enter your username");
            etUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
//        Log.e("ffff", "1======"+username);
//        Log.e("ffff", "2======"+password);
        UserAPI.userApi.Login(username,password).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                        User user = new User();
//                        user.setUser_Name("dmm");
                user = response.body();
                if (user!=null){
                    Toast.makeText(LoginActivity.this,"Login Successfully", Toast.LENGTH_LONG).show();
//                    Log.e("ffff", user.toString());
                    ObjectSharedPreferences.saveObjectToSharedPreference(LoginActivity.this, "User", "MODE_PRIVATE", user);
                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("object", user);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Incorrect UserName or Password", Toast.LENGTH_LONG).show();
                }
                Log.e("ffff", "Đăng nhập thành công");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Failed to connect, try again later", Toast.LENGTH_LONG).show();
                Log.e("ffff", "Kết nối API Login thất bại");
                Log.e("TAG", t.toString());
            }
        });
    }

    private void anhXa() {
        btnLogin = findViewById(R.id.btnSignUp);
        tvRegister = findViewById(R.id.tvRegister);
    }
}

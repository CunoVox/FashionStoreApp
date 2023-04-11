package com.example.fashionstoreapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionstoreapp.Adapter.CartAdapter;
import com.example.fashionstoreapp.Adapter.CheckOutAdapter;
import com.example.fashionstoreapp.Model.Address;
import com.example.fashionstoreapp.Model.Cart;
import com.example.fashionstoreapp.Model.Order;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.CartAPI;
import com.example.fashionstoreapp.Retrofit.OrderAPI;
import com.example.fashionstoreapp.SharedPrefManager.ObjectSharedPreferences;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    ImageView ivBack;
    TextView tvUserName, tvAddress, tvTotalPrice, tvPhoneNumber, tvChangeAddress, tvAddAddress;
    Button btnPlaceOrder;
    ConstraintLayout constraintLayoutAddress, constraintLayoutNotAddress, placeOrder, placeOrderSuccess;

    RecyclerView.Adapter checkOutAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        AnhXa();
        LoadAddress();
        LoadProductItem();
        ivBackClick();
        constraintLayoutNotAddressClick();
        tvChangeAddressClick();
        btnPlaceOrderClick();
    }

    private void btnPlaceOrderClick() {
        btnPlaceOrder.setOnClickListener(v -> {
            Address address = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "address", "MODE_PRIVATE", Address.class);
            if(address==null){
                Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Please add your address before place order", Toast.LENGTH_SHORT).show();
            }
            else {
                User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);
                OrderAPI.orderAPI.placeOrder(user.getId(), tvUserName.getText().toString(),
                        tvPhoneNumber.getText().toString().replace("(","").replace(")",""), tvAddress.getText().toString()).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Order order = response.body();
                        if (order!= null){
                            placeOrder.setVisibility(View.GONE);
                            placeOrderSuccess.setVisibility(View.VISIBLE);
                            Button btnContinueShopping = findViewById(R.id.btnContinueShopping);
                            btnContinueShopping.setOnClickListener(v1 -> {
                                startActivity(new Intent(CheckOutActivity.this, MainActivity.class));
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void tvChangeAddressClick() {
        tvChangeAddress.setOnClickListener(v -> {
            startActivity(new Intent(CheckOutActivity.this, AddressActivity.class));

        });
    }

    private void constraintLayoutNotAddressClick() {
        constraintLayoutNotAddress.setOnClickListener(v -> {
            startActivity(new Intent(CheckOutActivity.this, AddressActivity.class));
        });
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void LoadProductItem() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(linearLayoutManager);
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);
        CartAPI.cartAPI.cartOfUser(user.getId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                List<Cart> listCart = response.body();
                User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);
//                Log.e("++==", String.valueOf(listCart));

                checkOutAdapter = new CheckOutAdapter(listCart, CheckOutActivity.this);
                recyclerView.setAdapter(checkOutAdapter);

                //load total price
                int Total= 0;
                for(Cart y: listCart){
                    Total += y.getCount()*y.getProduct().getPrice();
                }
                Locale localeEN = new Locale("en", "EN");
                NumberFormat en = NumberFormat.getInstance(localeEN);
                tvTotalPrice.setText(en.format(Total));
            }
            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("====", "Call API Cart of user fail");
            }
        });
    }

    private void LoadAddress() {
        Address address = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "address", "MODE_PRIVATE", Address.class);
        if(address ==null){
            constraintLayoutAddress.setVisibility(View.GONE);
            constraintLayoutNotAddress.setVisibility(View.VISIBLE);
        }
        else {
            tvPhoneNumber.setText("("+address.getPhoneNumber()+")");
            tvUserName.setText(address.getFullName());
            tvAddress.setText(address.getAddress());
        }

    }

    private void AnhXa() {
        ivBack = findViewById(R.id.ivBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        tvAddAddress = findViewById(R.id.tvAddAddress);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        constraintLayoutAddress = findViewById(R.id.constraintLayoutAddress);
        constraintLayoutNotAddress = findViewById(R.id.constraintLayoutNotAddress);
        placeOrderSuccess = findViewById(R.id.placeOrderSuccess);
        placeOrder = findViewById(R.id.placeOrder);
    }
}

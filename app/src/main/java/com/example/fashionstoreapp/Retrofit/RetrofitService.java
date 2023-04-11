package com.example.fashionstoreapp.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

//    private String IPAddress="172.20.10.4";
//    private String IPAddress="192.168.1.19";
    private String IPAddress="192.168.163.1";
//    private String IPAddress="172.16.30.143";


    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    private Retrofit retrofit = null;

    public RetrofitService(){
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://"+IPAddress+":8080").addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}

package com.example.fashionstoreapp.Retrofit;

import com.example.fashionstoreapp.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {

    RetrofitService retrofitService = new RetrofitService();
    UserAPI userApi = retrofitService.getRetrofit().create(UserAPI.class);


    //    @Headers("Accept: application/json; charset=utf-8")

    @GET("/login")
    Call<User> Login(@Query("id") String id, @Query("password") String password);

    @FormUrlEncoded
    @POST("/signup")
    Call<User> SignUp(@Field("username") String username, @Field("fullname") String fullname, @Field("email") String email, @Field("password")String password);
}

package com.example.gildonaitemp.api;

import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableOverviewResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.ConsumableCarUpdateRequest;
import com.example.gildonaitemp.dto.NotificationRequest;
import com.example.gildonaitemp.dto.NotificationResponse;
import com.example.gildonaitemp.dto.ServerUserLoginRequest;
import com.example.gildonaitemp.dto.UserResponse;
import com.example.gildonaitemp.dto.UserRegisterRequest;
import com.example.gildonaitemp.dto.ServerUserUpdateRequest;
import com.example.gildonaitemp.dto.WeeklyDrivingPatternsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @POST("/api/users")
    Call<UserResponse> createUser(@Body UserRegisterRequest user);

    @POST("/api/users/login")
    Call<UserResponse> loginUser(@Body ServerUserLoginRequest loginRequest);

    @GET("api/users/loginId")
    Call<UserResponse> getUserByLoginId(@Query("loginId") String loginId);

    @GET("/api/users/{userId}")
    Call<UserResponse> getUserById(@Path("userId") String userId);

    @PUT("/api/users/{userId}")
    Call<UserResponse> updateUser(@Path("userId") String userId, @Body ServerUserUpdateRequest request);

    @GET("/api/patterns/user/{userId}/weekly-averages")
    Call<List<WeeklyDrivingPatternsResponse>> getWeeklyDrivingPatterns(@Path("userId") String userId);

    @POST("/api/consumables")
    Call<ConsumableResponse> createConsumable(@Body ConsumableCarUpdateRequest request);

    @GET("api/consumables/user/{userId}")
    Call<List<ConsumableResponse>> getConsumablesByUser(@Path("userId") String userId);

    @GET("/api/consumables/user/{userId}/overview")
    Call<ConsumableOverviewResponse> getConsumableOverview(@Path("userId") String userId);

    @GET("/api/car-models")
    Call<List<CarModelResponse>> getAllCarModels();

    @GET("/api/car-models/model/{modelName}")
    Call<List<CarModelResponse>> getCarModelsByModelName(@Path("modelName") String modelName);

    @GET("api/notifications/user/{userId}")
    Call<List<NotificationResponse>> getNotificationsByUserId(@Path("userId") String userId);

}



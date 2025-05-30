package com.example.gildonaitemp.api;

import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableOverviewResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.ConsumableRequest;
import com.example.gildonaitemp.dto.DrivingPatternResponse;
import com.example.gildonaitemp.dto.ServerUserLoginRequest;
import com.example.gildonaitemp.dto.ServerUserResponse;
import com.example.gildonaitemp.dto.ServerUserRequest;
import com.example.gildonaitemp.dto.ServerUserUpdateRequest;
import com.example.gildonaitemp.dto.WeeklyDrivingPatterns;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
//import com.kakao.sdk.user.model.UserResponse;

import java.util.List;

public interface ApiService {
    @POST("/api/users")
    Call<ServerUserResponse> createUser(@Body ServerUserRequest user);

    @POST("/api/users/login")
    Call<ServerUserResponse> loginUser(@Body ServerUserLoginRequest loginRequest);

    @GET("/api/users/{userId}")
    Call<ServerUserResponse> getUserById(@Path("userId") String userId);

    @PUT("/api/users/{userId}")
    Call<ServerUserResponse> updateUser(@Path("userId") String userId, @Body ServerUserUpdateRequest request);

    @GET("/api/patterns/user/{userId}")
    Call<List<DrivingPatternResponse>> getDrivingPatterns(@Path("userId") String userId);

    @GET("/api/patterns/user/{userId}/weekly-averages")
    Call<List<WeeklyDrivingPatterns>> getWeeklyDrivingPatterns(@Path("userId") String userId);

    @POST("/api/consumables")
    Call<ConsumableResponse> createConsumable(@Body ConsumableRequest request);

    @GET("api/consumables/user/{userId}")
    Call<List<ConsumableResponse>> getConsumablesByUser(@Path("userId") String userId);

    @GET("/api/consumables/user/{userId}/overview")
    Call<ConsumableOverviewResponse> getConsumableOverview(@Path("userId") String userId);

    @GET("/api/car-models")
    Call<List<CarModelResponse>> getAllCarModels();

    @GET("/api/car-models/model/{modelName}")
    Call<List<CarModelResponse>> getCarModelsByModelName(@Path("modelName") String modelName);
}



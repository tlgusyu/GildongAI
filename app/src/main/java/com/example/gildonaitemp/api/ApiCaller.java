package com.example.gildonaitemp.api;

import android.util.Log;

import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableCarUpdateRequest;
import com.example.gildonaitemp.dto.ConsumableOverviewResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.NotificationRequest;
import com.example.gildonaitemp.dto.NotificationResponse;
import com.example.gildonaitemp.dto.ServerUserLoginRequest;
import com.example.gildonaitemp.dto.ServerUserUpdateRequest;
import com.example.gildonaitemp.dto.UserRegisterRequest;
import com.example.gildonaitemp.dto.UserResponse;
import com.example.gildonaitemp.dto.WeeklyDrivingPatternsResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiCaller {

    public static void registerUser(UserRegisterRequest request, ResponseCallback<UserResponse> callback) {
        if (request == null) {
            Log.e("ApiCaller", "registerUser: request is null");
            return;
        }
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.createUser(request);
        call.enqueue(callback);
    }

    public static void loginUser(String loginId, String password, ResponseCallback<UserResponse> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.loginUser(new ServerUserLoginRequest(loginId, password));
        call.enqueue(callback);
    }

    public static void getUserByLoginId(String loginId, ResponseCallback<UserResponse> callback) {
        if (loginId == null || loginId.isEmpty()) {
            Log.e("ApiCaller", "getUserByLoginId: loginId is null or empty");
            return;
        }
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserByLoginId(loginId);
        call.enqueue(callback);
    }

    public static void getUserById(String userId, ResponseCallback<UserResponse> callback) {
        if (userId == null) {
            Log.e("ApiCaller", "userId is null");
            return;
        }
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserById(userId);
        call.enqueue(callback);
    }

    public static void updateUser(String userId, ServerUserUpdateRequest updateRequest, ResponseCallback<UserResponse> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.updateUser(userId, updateRequest);
        call.enqueue(callback);
    }

    public static void getWeeklyDrivingPatterns(String userId, ResponseCallback<List<WeeklyDrivingPatternsResponse>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<WeeklyDrivingPatternsResponse>> call = apiService.getWeeklyDrivingPatterns(userId);
        call.enqueue(callback);
    }

    public static void createConsumableCar(ConsumableCarUpdateRequest request, ResponseCallback<ConsumableResponse> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumableResponse> call = apiService.createConsumable(request);
        call.enqueue(callback);
    }

    public static void getConsumablesByUser(String userId, ResponseCallback<List<ConsumableResponse>> callback) {
        if (userId == null) {
            Log.e("ApiCaller", "userId is null");
            return;
        }
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ConsumableResponse>> call = apiService.getConsumablesByUser(userId);
        call.enqueue(callback);
    }

    public static void getConsumableOverview(String userId, ResponseCallback<ConsumableOverviewResponse> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumableOverviewResponse> call = apiService.getConsumableOverview(userId);
        call.enqueue(callback);
    }

    public static void getAllCarModels(ResponseCallback<List<CarModelResponse>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CarModelResponse>> call = apiService.getAllCarModels();
        call.enqueue(callback);
    }

    public static void getCarModelsByModelName(String carModel, ResponseCallback<List<CarModelResponse>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CarModelResponse>> call = apiService.getCarModelsByModelName(carModel);
        call.enqueue(callback);
    }

    public static void getNotificationsByUserId(String userId, ResponseCallback<List<NotificationResponse>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<NotificationResponse>> call = apiService.getNotificationsByUserId(userId);
        call.enqueue(callback);
    }

}

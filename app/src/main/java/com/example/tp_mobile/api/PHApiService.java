package com.example.tp_mobile.api;

import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.ResultLogin;
import com.example.tp_mobile.model.TimeSlot;
import com.example.tp_mobile.model.Token;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PHApiService {

    @GET("login.php")
    Call<Token> login(@Query("email") String email, @Query("password") String pwd);

    @GET("check_token.php")
    Call<ResultLogin> checkToken(@Query("token") String token);

    @GET("getHabitats_v3.php")
    Call<List<Habitat>> getHabitats(@Query("token") String token);

    @GET("getHabitat.php")
    Call<Habitat> getHabitat(@Query("token") String token);

    @GET("get_appliances_timeslot.php")
    Call<List<TimeSlot>> getTimeSlots(@Query("token") String token, @Query("month") String month); // Format attendu: YYYY-MM

    @GET("save_appliance_time_slot.php")
    Call<ResponseBody> saveApplianceTimeSlot(
            @Query("token") String token,
            @Query("appliance_id") int applianceId,
            @Query("time_slot_id") int timeSlotId,
            @Query("order") int order
    );
}

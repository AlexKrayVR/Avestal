package yelm.io.avestal.rest;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import yelm.io.avestal.rest.responses.AccessToken;
import yelm.io.avestal.rest.responses.AuthResponse;
import yelm.io.avestal.rest.responses.items.ItemsObject;
import yelm.io.avestal.rest.responses.service.Service;
import yelm.io.avestal.rest.responses.user.UserInfo;
import yelm.io.avestal.rest.responses.user_responses.UserResponse;

public interface RestAPI {

    String URL_API_MAIN = "https://api.avestal.ru/api/";

    @FormUrlEncoded
    @POST("response?")
    Call<ResponseBody> response(
            @Header("Authorization") String authHeader,
            @Field("service_id") String id
    );


    @GET("responses?")
    Call<List<UserResponse>> getResponses(
            @Header("Authorization") String authHeader
    );


    @FormUrlEncoded
    @POST("code?")
    Call<AuthResponse> code(
            @Field("phone") String phone
    );

    @GET("services?")
    Call<Service> getServices(
            @Header("Authorization") String authHeader
    );

    @FormUrlEncoded
    @POST("signup?")
    Call<ResponseBody> setUserData(
            @Field("phone") String phone,
            @Field("fio") JSONObject fio,
            @Field("data") JSONObject data
    );

    @FormUrlEncoded
    @POST("auth?")
    Call<AccessToken> getAccessToken(
            @Field("phone") String phone
    );


    @GET("user?")
    Call<UserInfo> getUserInfo(
            @Header("Authorization") String authHeader
    );

    @GET("items?")
    Call<ItemsObject> getItems(
            @Header("Authorization") String authHeader
    );

}
package yelm.io.avestal.rest;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yelm.io.avestal.Logging;

public class UploadImage {

    static public void upload(File file, Uri uri, Context context) {


        Logging.logDebug("upload()");

        //RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(context.getContentResolver().getType(uri)),
                        file
                );

        MultipartBody.Part body  = MultipartBody.Part
                .createFormData("image", file.getName(), requestFile);

        //RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "image");


        RetrofitClient.
                getClient(RestAPI.URL_API_MAIN)
                .create(RestAPI.class)
                .upload(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            if (response.body() != null) {
//                                Logging.logDebug("link()"+response.body());
//
//                            } else {
//                                Logging.logError("Method upload() - by some reason response is null!");
//                            }
                        } else {
                            Logging.logError("Method upload() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Logging.logError("Method upload() - failure: " + t.toString());
                    }
                });

    }
}

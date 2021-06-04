package yelm.io.avestal.rest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface KotlinAPI {

    @Multipart
    @POST("image")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("image") desc: RequestBody
    ): Call<ResponseBody>

    companion object {
        operator fun invoke(): KotlinAPI {
            return Retrofit.Builder()
                .baseUrl("https://api.avestal.ru/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KotlinAPI::class.java)
        }
    }


}
package com.example.zeew.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {
    //For Register
    @FormUrlEncoded
    @POST("CustomerSignUp")
    fun userRegister(
        @Field("action") action: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastname: String,
        @Field("username") username: String,
        @Field("phone_number") phoneNumber: String,
        @Field("user_country_code") userCountryCode: String,
        @Field("password") password: String,
        @Field("referral_code") referralCode: String
    ): Call<ResponseBody>

    //For Login
    @FormUrlEncoded
    @POST("CustomerLogin")
    fun userLogin(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("device_type") deviceType: String
    ): Call<ResponseBody>

    // this below fuctions used for retrofit calling functions
    companion object {
        operator fun invoke(): MyApi {
            return Retrofit.Builder()
                .baseUrl("https://global.zeew.eu/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}
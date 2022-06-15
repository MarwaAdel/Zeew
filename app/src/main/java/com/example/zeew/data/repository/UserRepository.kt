package com.example.zeew.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zeew.data.network.MyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository {
    //Register
    fun userRegister(firstName: String, lastName: String,email:String,phone:String,password: String,confirmPassword:String,referralCode:String):
            LiveData<String> {
        val registerResponse = MutableLiveData<String>()

        MyApi().userRegister("CustomerSignUp",firstName, lastName,email, "+20",phone,password,referralCode)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    registerResponse.value = t.message
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if (response.isSuccessful) {
                        registerResponse.value = response.body()?.string()
                    } else {
                        registerResponse.value = response.errorBody()?.string()

                    }
                }
            })

        return registerResponse
    }

    //Login
    fun userLogin(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

        MyApi().userLogin(email, password, "FCM_TOKEN", "ANDROID")
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    loginResponse.value = t.message
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if (response.isSuccessful) {
                        loginResponse.value = response.body()?.string()
                    } else {
                        loginResponse.value = response.errorBody()?.string()

                    }
                }
            })

        return loginResponse
    }
}
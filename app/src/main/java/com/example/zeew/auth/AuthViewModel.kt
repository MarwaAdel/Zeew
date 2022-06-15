package com.example.zeew.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.zeew.data.repository.UserRepository

class AuthViewModel : ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null


    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid Username or Password")
            return
        } else {
            val loginResponseFromUserRepository = UserRepository().userLogin(email!!, password!!)
            authListener?.onSuccess(loginResponseFromUserRepository)
        }

    }


}
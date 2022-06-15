package com.example.zeew.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.zeew.data.repository.UserRepository
import com.example.zeew.ui.auth.AuthListener

class SignUpViewModel : ViewModel() {

    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var referralCode: String? = null
    var authListener: AuthListener? = null


    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()
        if (firstName.isNullOrEmpty() || lastName.isNullOrEmpty() ||
            email.isNullOrEmpty() || phoneNumber.isNullOrEmpty() ||
            password.isNullOrEmpty() || confirmPassword.isNullOrEmpty() ||
            referralCode.isNullOrEmpty()
        ) {
            authListener?.onFailure("fields is mandatory! please fill it")
            return
        } else {

            val loginResponseFromUserRepository = UserRepository().userRegister(
                firstName!!, lastName!!, email!!, phoneNumber!!, password!!, confirmPassword!!,
                referralCode!!
            )
            authListener?.onSuccess(loginResponseFromUserRepository)

        }

    }

}
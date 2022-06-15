package com.example.zeew.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import com.example.zeew.R
import com.example.zeew.auth.SignUpViewModel
import androidx.lifecycle.Observer
import com.example.zeew.databinding.ActivitySignupBinding
import com.example.zeew.util.hide
import com.example.zeew.util.myToast
import com.example.zeew.util.show
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity(), AuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signup)

        var view = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        binding.view = view
        view.authListener = this
    }

    override fun onStarted() {
        progressBarSignUp.show()
        myToast("SignUp Started")
    }

    override fun onSuccess(loginResponseFromUserRepository: LiveData<String>) {
        loginResponseFromUserRepository.observe(this, Observer
        {
            progressBarSignUp.hide()
            myToast(it)
        })
    }


    override fun onFailure(message: String) {
        progressBarSignUp.hide()
        myToast(message)
    }
}




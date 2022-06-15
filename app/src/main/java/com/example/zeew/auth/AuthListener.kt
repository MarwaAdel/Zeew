package com.example.zeew.ui.auth

import androidx.lifecycle.LiveData

interface AuthListener {
    fun onStarted()
    fun onSuccess(loginResponseFromUserRepository: LiveData<String>)
    fun onFailure(message:String)
}
package com.example.zeew.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.zeew.R
import com.example.zeew.databinding.ActivityLoginBinding
import com.example.zeew.util.hide
import com.example.zeew.util.myToast
import com.example.zeew.util.show
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity(), AuthListener {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        var viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this

        val button = findViewById<TextView>(R.id.sign_up_clicked)
        button.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("951919024060-qq982tbmio6mi7fs9loealmcpati1rk9.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        google_login_btn.setOnClickListener {
            signIn()
        }


        //init
        loginButton = findViewById(R.id.fb_login_button)
        callbackManager = CallbackManager.Factory.create()
        loginButton.setPermissions(listOf("email", "user_birthday"))

        //login callback
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult?) {
                val userId = loginResult?.accessToken?.userId
                Log.d(TAG, "onSuccess: userId $userId")

                val bundle = Bundle()
                bundle.putString("fields", "id, email, first_name, last_name, gender,age_range")


                //Graph API to access the data of user's facebook account
                val request = GraphRequest.newMeRequest(
                    loginResult?.accessToken
                ) { fbObject, response ->
                    Log.v("Login Success", response.toString())


                    //For safety measure enclose the request with try and catch
                    try {

                        Log.d(TAG, "onSuccess: fbObject $fbObject")

                        val firstName = fbObject.getString("first_name")
                        val lastName = fbObject.getString("last_name")
                        val gender = fbObject.getString("gender")
                        val email = fbObject.getString("email")

                        Log.d(TAG, "onSuccess: firstName $firstName")
                        Log.d(TAG, "onSuccess: lastName $lastName")
                        Log.d(TAG, "onSuccess: gender $gender")
                        Log.d(TAG, "onSuccess: email $email")

                    } //If no data has been retrieve throw some error
                    catch (e: JSONException) {

                    }

                }
                //Set the bundle's data as Graph's object data
                request.setParameters(bundle)

                //Execute this Graph request asynchronously
                request.executeAsync()

            }

            override fun onCancel() {
                Log.d(TAG, "onCancel: called")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, "onError: called")
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID", googleId)
            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)
            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)
            myToast("Sign successfully")

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    override fun onStarted() {
        progressBar.show()
        myToast("Login Started")

    }

    override fun onSuccess(loginResponseFromUserRepository: LiveData<String>) {
        loginResponseFromUserRepository.observe(this, Observer
        {
            progressBar.hide()
            myToast(it)
        })
    }

    override fun onFailure(message: String) {
        progressBar.hide()
        myToast(message)

    }


}

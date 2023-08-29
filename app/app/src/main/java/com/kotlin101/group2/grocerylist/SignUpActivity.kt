package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.SignInRequest
import com.kotlin101.group2.grocerylist.data.api.models.SignUpRequest
import com.kotlin101.group2.grocerylist.data.api.models.TextResponse
import com.kotlin101.group2.grocerylist.data.api.models.UserResponse
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.getInstance()
        pref = GroceryAppSharedPreference.getInstance(this)

        changePageState(isLoading = false)

        with(binding){
            btnSignUp.setOnClickListener {
                startValidation()
            }
            tvSignIn.setOnClickListener {
                gotoSignIn()
            }
        }
    }

    private fun changePageState(isLoading:Boolean){
        with(binding){
            when(isLoading){
                true->{
                    pbLoading.visibility = View.VISIBLE
                    btnSignUp.visibility = View.INVISIBLE
                    tvSignIn.isEnabled = false
                }
                false->{
                    pbLoading.visibility = View.GONE
                    btnSignUp.visibility = View.VISIBLE
                    tvSignIn.isEnabled = true
                }
            }
        }
    }

    private fun startValidation() {
        with(binding){
            var errors: Int = 0
            if (etEmail.text.toString().isEmpty()){
                errors += 1
                tvEmail.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.red))
            }

            if (etName.text.toString().isEmpty()){
                errors += 1
                tvName.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.red))
            }

            if (etPassword.text.toString().length < 8){
                errors += 1
                tvPassword.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.red))
            }

            if (errors == 0){
                processSignUp()
            }
        }
    }

    private fun processSignUp() {
        with(binding){
            changePageState(isLoading = true)
            GlobalScope.launch {
                var signUp = api.signUp(SignUpRequest(etEmail.text.toString(), etName.text.toString(), etPassword.text.toString()))
                withContext(Dispatchers.Main){
                    btnSignUp.isEnabled = false
                }
                if (signUp.isSuccessful){
                    getSignInToken()
                }else{
                    val errorResponse = Gson().fromJson(signUp.errorBody()!!.charStream(), TextResponse::class.java)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignUpActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                        btnSignUp.isEnabled = true
                    }
                    changePageState(isLoading = false)
                }
            }
        }
    }

    private fun getSignInToken() {
        with(binding){
            GlobalScope.launch {
                var tokenRequest = api.signIn(SignInRequest(etEmail.text.toString(),etPassword.text.toString()))
                if (tokenRequest.isSuccessful){
                    val token = tokenRequest.body()?.text
                    var userRequest = api.getCurrentUserInfo("bearer $token")
                    if (userRequest.isSuccessful){
                        var user = userRequest.body()
                        if (user != null){
                            pref.setToken(token!!)
                            pref.setUser(UserResponse(user.cartId, user.cartItems, user.email, user.name, user.avatar))

                            withContext(Dispatchers.Main) {
                                gotoUpdateProfile()
                            }
                        }
                    }
                }else{
                    val errorResponse = Gson().fromJson(tokenRequest.errorBody()!!.charStream(), TextResponse::class.java)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignUpActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                        btnSignUp.isEnabled = true
                    }
                }
                withContext(Dispatchers.Main){
                    changePageState(isLoading = false)
                }
            }
        }
    }

    private fun gotoUpdateProfile() {
        startActivity(Intent(this@SignUpActivity, UpdateProfileActivity::class.java))
        finish()
    }

    private fun gotoSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
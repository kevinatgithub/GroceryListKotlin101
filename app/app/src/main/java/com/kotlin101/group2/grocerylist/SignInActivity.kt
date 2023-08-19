package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.SignInRequest
import com.kotlin101.group2.grocerylist.data.api.models.TextResponse
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivitySignInBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.GetInstance()
        pref = GroceryAppSharedPreference.getInstance(this)

        if (!pref.getUser()!!.email.isEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        with(binding) {
            tvSignUp.setOnClickListener {
                gotoSignUp()
            }
            btnSignIn.setOnClickListener {
                startSignInValidation()
            }
        }
    }

    private fun startSignInValidation() {
        with(binding){
            var errors: Int  = 0
            if (etEmail.text!!.isEmpty()){
                errors++
                tvEmail.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.red))
            }

            if (etPassword.text!!.isEmpty()){
                errors++
                tvPassword.setTextColor(ContextCompat.getColor(this@SignInActivity, R.color.red))
            }

            if (errors == 0){
                startSignIn()
            }
        }
    }

    private fun startSignIn() {
        with(binding){
            btnSignIn.isEnabled = false
            GlobalScope.launch {
                var signinRequest = api.signIn(SignInRequest(etEmail.text!!.toString(), etPassword.text!!.toString()))
                if (signinRequest.isSuccessful){
                    val token = signinRequest.body()!!.text
                    pref.setToken(token)
                    var user = pref.getUser()
                    if (user.email.isEmpty()){
                        val userRequest = api.getCurrentUserInfo("bearer $token")
                        if (userRequest.isSuccessful){
                            user = userRequest.body()!!
                            pref.setUser(user)
                            withContext(Dispatchers.Main){
                                gotoMain()
                            }
                        }else{
                            val errorResponse = Gson().fromJson(signinRequest.errorBody()!!.charStream(), TextResponse::class.java)
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@SignInActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                                btnSignIn.isEnabled = true
                            }
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            gotoMain()
                        }
                    }
                }else{
                    val errorResponse = Gson().fromJson(signinRequest.errorBody()!!.charStream(), TextResponse::class.java)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignInActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                        btnSignIn.isEnabled = true
                    }
                }
            }
        }
    }

    private fun gotoMain() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    private fun gotoSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}

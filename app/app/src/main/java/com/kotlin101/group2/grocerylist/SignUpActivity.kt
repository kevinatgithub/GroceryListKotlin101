package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.databinding.ActivitySignUpBinding

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tvSignIn.setOnClickListener {
                gotoSignIn()
            }
        }
    }

    private fun gotoSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
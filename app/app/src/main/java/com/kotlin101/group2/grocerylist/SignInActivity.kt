package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            tvSignUp.setOnClickListener {
                gotoSignUp()
            }
        }
    }

    private fun gotoSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}

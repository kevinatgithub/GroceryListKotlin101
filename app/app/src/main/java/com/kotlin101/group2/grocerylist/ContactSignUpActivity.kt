package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.databinding.ActivityContactSignUpBinding

class ContactSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            layoutHeader.tvTitle.text = "Add Contact"
            layoutHeader.ivBack.setOnClickListener {
                finish()
            }
            btnSignUp.setOnClickListener {
                gotoSignUp()
            }
        }
    }

    private fun gotoSignUp() {
        startActivity(Intent(this, ContactSignUpActivity::class.java))
        finish()
    }
}
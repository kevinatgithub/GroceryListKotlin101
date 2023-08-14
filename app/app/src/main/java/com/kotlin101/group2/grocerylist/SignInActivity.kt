package com.kotlin101.group2.grocerylist

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
            tvTitle1.text = getString(R.string.group_II)
            tvTitle2.text = getString(R.string.app_name)
            tvTitle3.text = getString(R.string.app)
            etEmail.setText(getString(R.string.email))
            tvEmail.text = (getString(R.string.helper_text))
            etPassword.text = null //Clear any existing text
            tvPassword.text = (getString(R.string.password_characters))
            btnSignIn.text = getString(R.string.sign_in)
            tvSignUpPrompt.text = getString(R.string.no_account)
            tvSignUp.text = getString(R.string.sign_up)
        }
    }
}

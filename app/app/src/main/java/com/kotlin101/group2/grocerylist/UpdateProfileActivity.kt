package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var pref: GroceryAppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = GroceryAppSharedPreference.getInstance(this)

        with(binding){
            pageHeader.tvPageTitle.text = "Profile"
            pageHeader.ivBack.setOnClickListener {
                var token = pref.getToken()
                if (token!!.isEmpty()){
                    startActivity(Intent(this@UpdateProfileActivity, SignInActivity::class.java))
                }else{
                    startActivity(Intent(this@UpdateProfileActivity, MainActivity::class.java))
                }
                finish()
            }

            btnUpdate.setOnClickListener {
                startUpdateProfile()
            }
        }
    }

    private fun startUpdateProfile() {
        TODO("Not yet implemented")
        startActivity(Intent(this@UpdateProfileActivity, MainActivity::class.java))
        finish()
    }
}
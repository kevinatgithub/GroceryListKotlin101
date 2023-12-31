package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.UpdateProfileRequest
import com.kotlin101.group2.grocerylist.data.api.models.UserResponse
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityUpdateProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProfileActivity : AppCompatActivity() {
    private var profileUrl: String? = null
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference
    private lateinit var user: UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        api = GroceryApiBuilder.getInstance()
        pref = GroceryAppSharedPreference.getInstance(this)
        user = pref.getUser()
        profileUrl = user.avatar

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

            etEmail.setText(user.email)
            etName.setText(user.name)
            if (user.avatar != null){
                Picasso.get().load(user.avatar).into(profileImage)
            }

            profileImage.setOnClickListener {
                selectAvatar()
            }

            ivCamera.setOnClickListener {
                selectAvatar()
            }

            btnSignOut.setOnClickListener {
                confirmSignOut()
            }
        }
    }

    private fun confirmSignOut() {
        AlertDialog.Builder(this).apply {
            title = "Sign out"
            setMessage("Are you sure you wan't to sign out? Signing back in to the app will require internet connection.")
            setPositiveButton(android.R.string.yes){ dialog, which ->
                pref.setToken("")
                startActivity(Intent(this@UpdateProfileActivity, SignInActivity::class.java))
                finish()
            }
            setNegativeButton(android.R.string.no){ dialog, which ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun selectAvatar() {
        val intent = Intent(this,SelectAvatarActivity::class.java)
        selectAvatarRequest.launch(intent)
    }

    private fun startUpdateProfile() {
        var errors = 0
        with(binding){
            if (etName.text!!.isEmpty()){
                errors++
            }

            if (!etNewPassword.text!!.isEmpty() && etOldPassword.text!!.isEmpty()){
                errors++
            }

            if (errors > 0){
                Toast.makeText(this@UpdateProfileActivity,"Please provide name", Toast.LENGTH_LONG).show()
            }else{
                changePageState(isLoading = true)
                GlobalScope.launch {
                    val result = api.updateProfile(UpdateProfileRequest(etName.text.toString(), etOldPassword.text.toString(), etNewPassword.text.toString(), profileUrl ), pref.getToken().toString())

                    if (result.isSuccessful){
                        withContext(Dispatchers.Main){
                            user.name = etName.text.toString()
                            user.avatar = profileUrl
                            pref.setUser(user)
                            Toast.makeText(this@UpdateProfileActivity, "Profile has been updated!",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@UpdateProfileActivity, MainActivity::class.java))
                            changePageState(isLoading = false)
                            finish()
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@UpdateProfileActivity, "Update profile failed!", Toast.LENGTH_SHORT).show()
                            changePageState(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun changePageState(isLoading:Boolean){
        with(binding){
            var btnState : Boolean = false
            when(isLoading){
                true->{
                    pbLoading.visibility = View.VISIBLE
                    btnState = false
                }
                false->{
                    pbLoading.visibility = View.GONE
                    btnState = true
                }
            }
            btnUpdate.isEnabled = btnState
            btnSignOut.isEnabled = btnState
            pageHeader.ivBack.isEnabled = btnState
        }
    }

    private val selectAvatarRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val url = intent.getStringExtra(ImageSearchActivity.IMAGE_URL)
                if (url != null){
                    profileUrl = url
                    Picasso.get().load(url).into(binding.profileImage)
                }
            }
        }
    }
}
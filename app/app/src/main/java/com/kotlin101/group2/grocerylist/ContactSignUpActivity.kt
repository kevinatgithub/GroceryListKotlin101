package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.ContactSignUpRequest
import com.kotlin101.group2.grocerylist.data.api.models.TextResponse
import com.kotlin101.group2.grocerylist.data.api.models.UserResponse
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityContactSignUpBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactSignUpBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference
    private lateinit var user: UserResponse
    private var profileUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        api = GroceryApiBuilder.getInstance()
        pref = GroceryAppSharedPreference.getInstance(this)
        user = pref.getUser()

        with(binding) {
            layoutHeader.tvTitle.text = "Add Contact"
            layoutHeader.ivBack.setOnClickListener {
                startActivity(Intent(this@ContactSignUpActivity, ContactsActivity::class.java))
                finish()
            }

            if (!user.avatar!!.isEmpty()){
                Picasso.get().load(user.avatar).into(binding.settingProfileImage)
            }

            tvContactName.text = user.name

            btnSignUp.setOnClickListener {
                processContactSignUp()
            }

            photoBox.setOnClickListener {
                val i = Intent(this@ContactSignUpActivity, SelectAvatarActivity::class.java)
                selectAvatarRequest.launch(i)
            }
        }
    }

    private fun processContactSignUp() {
        var errors = 0
        with(binding){
            if (etEmail.text!!.isEmpty()){
                errors++
            }

            if (etName.text!!.isEmpty()){
                errors++
            }

            if (etPassword.text!!.isEmpty()){
                errors++
            }

            if (errors == 0){
                changePageState(true)
                GlobalScope.launch {
                    val signup = api.contactSignUp(ContactSignUpRequest(user.cartId,etEmail.text.toString(), etName.text.toString(),etPassword.text.toString(), profileUrl))
                    if (signup.isSuccessful){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ContactSignUpActivity, "Contact has been added!", Toast.LENGTH_LONG).show()
                            changePageState(false)
                            startActivity(Intent(this@ContactSignUpActivity, ContactsActivity::class.java))
                            finish()
                        }
                    }else{
                        val errorResponse = Gson().fromJson(signup.errorBody()!!.charStream(), TextResponse::class.java)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ContactSignUpActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                            changePageState(false)
                        }
                    }
                }
            }else{
                Toast.makeText(this@ContactSignUpActivity, "Please provide required information!", Toast.LENGTH_LONG).show()
                changePageState(false)
            }
        }
    }

    private val selectAvatarRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val url = intent.getStringExtra(ImageSearchActivity.IMAGE_URL)
                if (url != null){
                    profileUrl = url
                    Picasso.get().load(url).into(binding.photoBox)
                }
            }
        }
    }

    private fun changePageState(isLoading : Boolean){
        with(binding){
            layoutHeader.ivBack.isEnabled = !isLoading
            photoBox.isEnabled = !isLoading
            tvAddPhoto.isEnabled = !isLoading
            btnSignUp.isEnabled = !isLoading
            pbLoading.visibility = if (!isLoading) View.GONE else View.VISIBLE
        }
    }
}
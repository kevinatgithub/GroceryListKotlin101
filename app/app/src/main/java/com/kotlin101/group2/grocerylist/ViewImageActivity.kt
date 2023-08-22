package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.databinding.ActivityViewImageBinding
import com.squareup.picasso.Picasso

class ViewImageActivity: AppCompatActivity() {
    private lateinit var binding : ActivityViewImageBinding

    companion object{
        val URL : String = "URL"
        val SHOW_BUTTON : String = "SHOW_BUTTON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url: String? = intent.getStringExtra(URL)
        val showButton: Boolean? = intent.getBooleanExtra(SHOW_BUTTON,false)


        with(binding){
            layoutHeader.tvTitle.text = "Select Image"
            layoutHeader.ivBack.setOnClickListener {
                finish()
            }
            Picasso.get().load(url).into(ivImage)
            if (showButton == true){
                fabSelectImage.visibility = View.VISIBLE
                fabSelectImage.setOnClickListener {
                    returnResult()
                }
            }else{
                fabSelectImage.visibility = View.GONE
            }
        }
    }

    private fun returnResult() {
        val url: String? = intent.getStringExtra(URL)

        setResult(Activity.RESULT_OK, intent.apply {
            putExtra(URL, url)
        })

        finish()
    }
}
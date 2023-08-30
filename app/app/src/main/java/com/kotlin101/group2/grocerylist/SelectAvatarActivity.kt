package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ImageSearchAdapter
import com.kotlin101.group2.grocerylist.databinding.ActivitySelectAvatarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectAvatarActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySelectAvatarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            with(layoutHeader){
                tvTitle.text = "Select Profile Avatar"
                ivBack.setOnClickListener {
                    finish()
                }
            }
            rvAvatars.layoutManager = GridLayoutManager(this@SelectAvatarActivity,2)
            GlobalScope.launch {
                val images = GroceryAppHelpers.getRelatedImagesUrlFromWeb("profile cartoon avatar", 200)

                withContext(Dispatchers.Main){
                    val adapter = ImageSearchAdapter(images,::showAvatar)
                    rvAvatars.adapter = adapter
                }
            }
        }
    }

    fun showAvatar(url:String){
        val intent = Intent(this, ViewImageActivity::class.java)
        intent.apply {
            putExtra(ViewImageActivity.SHOW_BUTTON,true)
            putExtra(ViewImageActivity.URL,url)
        }
        viewImageRequest.launch(intent)
    }

    fun selectAvatar(url:String){
        val resultIntent = intent.apply {
            putExtra(ImageSearchActivity.IMAGE_URL, url)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private val viewImageRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val url = intent.getStringExtra(ViewImageActivity.URL)
                if (url != null){
                    selectAvatar(url)
                }
            }
        }
    }
}
package com.kotlin101.group2.grocerylist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.databinding.ActivityUpdateItemBinding


class UpdateItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
        }
    }
}

package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ImageSearchAdapter
import com.kotlin101.group2.grocerylist.databinding.ActivitySearchImageBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ImageSearchActivity : AppCompatActivity()  {
    private lateinit var binding: ActivitySearchImageBinding

    companion object{
        val KEYWORD : String = "KEYWORD"
        val IMAGE_URL : String = "IMAGE_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val keyword: String? = intent.getStringExtra(KEYWORD)

        with(binding){
            rvImages.layoutManager = GridLayoutManager(this@ImageSearchActivity,2)
            with(pageHeader){
                ivBack.setOnClickListener {
                    finish()
                }
                tvPageTitle.text = "Search Image"
            }

        }

        GlobalScope.launch {
            val resultUrls = getRelatedImagesUrlFromWeb(keyword!!)
            val adapter = ImageSearchAdapter(resultUrls, ::previewImage)
            withContext(Dispatchers.Main){
                binding.rvImages.adapter=adapter
            }
        }
    }

    fun previewImage(url: String){
        viewImageRequest.launch(Intent(this, ViewImageActivity::class.java).apply {
            putExtra(ViewImageActivity.URL, url)
            putExtra(ViewImageActivity.SHOW_BUTTON, true)
        })
    }

    fun onImageSelected(url: String){
        val resultIntent = intent.apply {
            putExtra(IMAGE_URL, url)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private val viewImageRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val url = intent.getStringExtra(ViewImageActivity.URL)
                if (url != null){
                    onImageSelected(url)
                }
            }
        }
    }

    private fun getRelatedImagesUrlFromWeb(keyword : String, resultCount : Int = 200) : ArrayList<String>{
        var url = "https://ph.images.search.yahoo.com/search/images;_ylt=AwrxhZU7hLZerxMAKr60Rwx.;_ylc=X1MDMjExNDczNDAwNARfcgMyBGZyAwRncHJpZANUM1AwS1ZEbFR2ZUxvVzBwMzRyQl9BBG5fc3VnZwMxMARvcmlnaW4DcGguaW1hZ2VzLnNlYXJjaC55YWhvby5jb20EcG9zAzAEcHFzdHIDBHBxc3RybAMEcXN0cmwDNgRxdWVyeQN0YWxvbmcEdF9zdG1wAzE1ODkwMTk3NTM-?fr2=sb-top-ph.images.search&p=KEYWORD&ei=UTF-8&iscqry=&fr=sfp"
        url = url.replace("KEYWORD", keyword)

        val result = ArrayList<String>()
        val document: Document = Jsoup.connect(url).get()
        val imgs : List<Element> = document.select("img").take(resultCount)
        imgs.forEach {
            val imgBase64 = it.attr("data-src")
            if (!imgBase64.isEmpty()){
                result.add(imgBase64)
            }
        }
        return result
    }
}
package com.kotlin101.group2.grocerylist

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object GroceryAppHelpers {

    fun getRelatedImagesUrlFromWeb(keyword : String, resultCount : Int = 200) : ArrayList<String>{
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
package com.kotlin101.group2.grocerylist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
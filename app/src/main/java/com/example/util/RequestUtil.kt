package com.example.util

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.base.TAG
import com.example.repository.model.CartoonInfor
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/4/1
 * Time: 21:16
 */

object RequestUtil {
    //msg3集数
    val msg3List: MutableList<CartoonInfor> by lazy { ArrayList() }
    private val msg3liveData by lazy { MutableLiveData<Boolean>() }
    val msg3LiveData
        get() = msg3liveData
    var content: String? = null
    var update: String? = null

    //remote
    private val remote = CartoonRemote
    private val errorLiveData = remote.error
    private val pgLiveData = remote.pgLiveData//加载监听

    //跳转传递数据
    val bundle by lazy { Bundle() }
    fun putBundle(name: String, img: String, href: String, mark: Int) {
        if (bundle.size() > 0)
            bundle.clear()
        bundle.putString("name", name)
        bundle.putString("img", img)
        bundle.putString("href", href)
        bundle.putInt("mark", mark)
    }

    /***加载漫画,判断漫画源**/
    fun loadCartoon(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (url.contains("wuqimh")) {
                remote.getData(url) {
                    pgLiveData.postValue(true)
                }.collect {
                    what357(it)
                }
                return@launch
            }
            var s = url
            if (!s.contains("dmzj"))
                s = Api.url2 + "/" + s//需要加"/"
            remote.getData(s) { pgLiveData.postValue(true) }
                .collect {
                    Log.i(TAG, "getHomeCartoon: $s")
                    what3(it)
                }
        }
    }


    private suspend fun what357(string: String) {
        val document = Jsoup.parse(string)
        val elements = document.getElementsByClass("chapter-list cf mt10")
        if (elements.text().isEmpty()) {
            pgLiveData.postValue(true)
            errorLiveData.postValue("此漫画无法浏览")
            return
        }
        content = document.select("#intro-cut").text()
        val elements1 = elements.select("li")
        var cartoonInfor: CartoonInfor
        for (e in elements1) {
            cartoonInfor = CartoonInfor(e.text(), Api.mh57Url + e.selectFirst("a").attr("href"))
            msg3List.add(cartoonInfor)
        }
        if (msg3List.size > 0) {
            msg3List.reverse()
            msg3LiveData.postValue(true)
        }
        delay(300)
        pgLiveData.postValue(true)
    }

    private suspend fun what3(string: String) {//集数
        val document = Jsoup.parse(string)
        val elements = document.getElementsByClass("cartoon_online_border")
//        Log.i(TAG, "what3: ${document}")
        if (elements.text().isEmpty()) {
            pgLiveData.postValue(true)
            errorLiveData.postValue("此漫画无法浏览")
            return
        }
        update = document.getElementsByClass("update2").text() ?: ""
        content = document.select(".line_height_content").text()
        val elements1 = elements.select("a")
        var cartoonInfor: CartoonInfor
        for (e in elements1) {
            cartoonInfor = CartoonInfor(e.text(), e.attr("href"))
            msg3List.add(cartoonInfor)
        }
        if (msg3List.size > 0) msg3LiveData.postValue(true)
        delay(300)
        pgLiveData.postValue(true)
    }
}
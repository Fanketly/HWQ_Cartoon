package com.example.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfo
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.example.util.RequestUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/5
 * Time: 19:58
 */
class SearchViewModel : ViewModel() {
    //remote
    private val remote = CartoonRemote
    private val errorLiveData = remote.error
    val bottomLiveData
        get() = remote.bottomLiveData
    val pgLiveData = remote.pgLiveData//加载监听

    //request
    private val requestUtil = RequestUtil

    /**判断是否在searchFragment**/
    var isSearchFragment = false

    val searchList: MutableList<CartoonInfo> by lazy { ArrayList() }
    val searchLiveData by lazy { MutableLiveData<Int>() }
    val searchList57: MutableList<CartoonInfo> by lazy { ArrayList() }
    private fun what5(s: String) {//查询
        if (s.isNotEmpty()) {
            val ss = s.split("{").toTypedArray()
            var cartoonInfor: CartoonInfo
            if (ss.size > 10) {
                var ss2: Array<String>
                var s51: String
                var s56: String
                var s54: String
                for (i in 1..10) {
                    ss2 = ss[i].split(",").toTypedArray()
                    //4 图 6 地址 1 名称
                    s51 = ss2[1]
                    s56 = ss2[6]
                    s54 = ss2[4]
                    cartoonInfor = CartoonInfo(
                        decode(s51.substring(14, s51.length - 1))!!,
                        "https://" + s56.substring(21, s56.length - 1)
                            .replace("\\", ""),
                        s54.substring(9, s54.length - 1).replace("\\", "")
                    )
                    searchList.add(cartoonInfor)
                }
            } else {
                var ss2: Array<String>
                var s51: String
                var s56: String
                var s54: String
                for (i in 1 until ss.size) {
                    ss2 = ss[i].split(",").toTypedArray()
                    //4 图 6 地址 1 名称
                    s51 = ss2[1]
                    s56 = ss2[6]
                    s54 = ss2[4]
                    cartoonInfor = CartoonInfo(
                        decode(s51.substring(14, s51.length - 1))!!,
                        "https://" + s56.substring(21, s56.length - 1)
                            .replace("\\", ""),
                        s54.substring(9, s54.length - 1).replace("\\", "")
                    )
                    searchList.add(cartoonInfor)
                }
            }
            if (searchJob.isActive)
                searchLiveData.postValue(1)
        } else {
            errorLiveData.postValue("动漫之家查询不到此漫画")
        }
    }


    /**
     * search
     */
    fun getSearch(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val cartoonInfor = searchList[position]
        val s = cartoonInfor.href
        requestUtil.putBundle(cartoonInfor.title, cartoonInfor.img, s, R.id.searchFragment)
        requestUtil.loadCartoon(s)
    }

    fun getSearch57(cartoonInfor: CartoonInfo) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val s = Api.mh57Url + cartoonInfor.href
        requestUtil.putBundle(cartoonInfor.title, cartoonInfor.img, s, R.id.searchFragment)
        requestUtil.loadCartoon(s)
    }

    private lateinit var searchJob: Job
    fun search(name: String?) {
        pgLiveData.value = false
        Log.i(TAG, "search: $name")
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            launch {
                remote.getData(Api.sacgUrl + "comicsum/search.php?s=$name")
                    .collect {
                        what5(it)
                    }
            }
            launch {
                remote.getData(Api.mh57Url + "/search/q_$name").collect {
                    mh57Search(it)
                }
            }
        }
    }


    fun clearSearchList() {
        Log.i(TAG, "clearSearchList: ")
        searchJob.cancel()
        if (searchList.size > 0) searchList.clear()
        if (searchList57.size > 0) searchList57.clear()
    }

    private fun mh57Search(string: String) {
        val jsoup = Jsoup.parse(string)
        val elements = jsoup.getElementsByClass("cf")
        if (elements.isEmpty()) {
            errorLiveData.postValue("57漫画查询不到此漫画")
            return
        }
        for (i in 2 until elements.size) {
            val e = elements[i]
            val a = e.select(".bcover")
            if (a.isEmpty())
                continue
            searchList57.add(
                CartoonInfo(
                    a.attr("title"),
                    a.attr("href"),
                    a.select("img").attr("src")
                )
            )
        }
        if (searchJob.isActive)
            searchLiveData.postValue(2)
    }

    /**
     * 逻辑处理部分
     */
    private fun decode(unicodeStr: String?): String? { //UNICODE转中文
        if (unicodeStr == null) {
            return null
        }
        val retBuf = StringBuilder()
        val maxLoop = unicodeStr.length
        var i = 0
        while (i < maxLoop) {
            if (unicodeStr[i] == '\\') {
                if (i < maxLoop - 5 && (unicodeStr[i + 1] == 'u' || unicodeStr[i + 1] == 'U')) try {
                    retBuf.append(unicodeStr.substring(i + 2, i + 6).toInt(16).toChar())
                    i += 5
                } catch (localNumberFormatException: NumberFormatException) {
                    retBuf.append(unicodeStr[i])
                } else retBuf.append(unicodeStr[i])
            } else {
                retBuf.append(unicodeStr[i])
            }
            i++
        }
        return retBuf.toString()
    }

}
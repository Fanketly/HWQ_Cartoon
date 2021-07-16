package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfo
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.example.util.RequestUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.jsoup.Jsoup

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/5
 * Time: 19:58
 */
class SearchViewModel @ViewModelInject constructor(
    private val requestUtil: RequestUtil,
    private val remote: CartoonRemote
) : ViewModel() {
    //remote
//    private val remote = CartoonRemote
    private val errorLiveData = remote.error
    val bottomLiveData
        get() = remote.bottomLiveData
    val pgLiveData = remote.pgLiveData//加载监听

    //request
//    private val requestUtil = RequestUtil

    /**判断是否在searchFragment**/
    var isSearchFragment = false

    val searchList: MutableList<CartoonInfo> by lazy { ArrayList() }
    val searchLiveData by lazy { MutableLiveData<Int?>() }
    val searchListYK: MutableList<CartoonInfo> by lazy { ArrayList() }
    private suspend inline fun searchDMZJ(url: String) {//查询
        remote.getData<CartoonInfo>(url, data = { s, flow ->
            if (s.isBlank()) {
                if (errorLiveData.value == "优酷漫画查询不到此漫画")
                    pgLiveData.postValue(true)
                errorLiveData.postValue("动漫之家查询不到此漫画")
                searchLiveData.postValue(1)
                return@getData
            }
            val ss = s.split("{").toTypedArray()
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
                    flow.emit(
                        CartoonInfo(
                            decode(s51.substring(14, s51.length - 1))!!,
                            "https://" + s56.substring(21, s56.length - 1)
                                .replace("\\", ""),
                            s54.substring(9, s54.length - 1).replace("\\", "")
                        )
                    )
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
                    flow.emit(
                        CartoonInfo(
                            decode(s51.substring(14, s51.length - 1))!!,
                            "https://" + s56.substring(21, s56.length - 1)
                                .replace("\\", ""),
                            s54.substring(9, s54.length - 1).replace("\\", "")
                        )
                    )
                }
            }

        }, success = {
            if (searchJob.isActive) {
                searchLiveData.postValue(1)
            }
        }, fail = {
            if (searchJob.isActive) {
                searchLiveData.postValue(1)
            }
        }).collect {
            searchList.add(it)
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

    fun getSearchYK(cartoonInfo: CartoonInfo) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val s = cartoonInfo.href
        requestUtil.putBundle(cartoonInfo.title, cartoonInfo.img, s, R.id.searchFragment)
        requestUtil.loadCartoon(s)
    }

    private lateinit var searchJob: Job
    fun search(name: String?) {
        pgLiveData.value = false
        Log.i(TAG, "search_name: $name")
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            launch {
                searchDMZJ(Api.sacgUrl + "comicsum/search.php?s=$name")
            }
            launch {
                remote.getData<CartoonInfo>(
                    Api.youkuUrl + "/search/?keywords=$name",
                    data = { data, flow ->
                        val jsoup = Jsoup.parse(data)
                        val elements =
                            jsoup.getElementsByClass("list_con_li update_con autoHeight").first()
                        if (elements == null) {
                            if (errorLiveData.value == "动漫之家查询不到此漫画")
                                pgLiveData.postValue(true)
                            errorLiveData.postValue("优酷漫画查询不到此漫画")
                            return@getData
                        }
                        for (element in elements.select("li")) {
                            val a = element.select("a").first()
                            val img = a.select("img").first()
                            flow.emit(
                                CartoonInfo(
                                    a.attr("title"),
                                    a.attr("href"),
                                    img.attr("src")
                                )
                            )
                        }
                    },
                    success = {
                        if (searchJob.isActive) {
                            delay(100)
                            searchLiveData.postValue(2)
                        }
                    }, fail = {
                        if (searchJob.isActive) {
                            delay(100)
                            searchLiveData.postValue(2)
                        }
                    }).collect {
                    searchListYK.add(it)
                }
            }
        }
    }


    fun clearSearchList() {
        Log.i(TAG, "clearSearchList: ")
        searchJob.cancel()
        if (searchList.size > 0) searchList.clear()
        if (searchListYK.size > 0) searchListYK.clear()
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
package com.example.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.*
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 20:35
 */

class CartoonViewModel : ViewModel() {
    init {
        Log.i(TAG, "CREATE: ")
    }

    lateinit var detailViewModel: DetailViewModel


    //msg2主页漫画
    val cartoonInfors: MutableList<CartoonInfor> = ArrayList()
    val homeLiveData = MutableLiveData<Boolean>()
    private var pager = 1

    //主页57推荐漫画
    private val homeRecommendList = ArrayList<CartoonInfor>()
    val homeRecommendLiveData = MutableLiveData<List<CartoonInfor>>()


    //banner
//    val bannerList: MutableList<CartoonInfor> = ArrayList()
//    val bannerLiveData = MutableLiveData<List<CartoonInfor>>()
    val bannerList by lazy { arrayListOf(R.drawable.lzsm1, R.drawable.lzsy2, R.drawable.am3) }

    //remote
    private val remote = CartoonRemote
    val errorLiveData = remote.error

    //监听是否隐藏bottom false为显示
    val bottomLiveData = remote.bottomLiveData

    //加载监听
    val pgLiveData = remote.pgLiveData


    /***加载漫画,判断漫画源**/
//    private fun loadCartoon(url: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (url.contains("wuqimh")) {
//                remote.getData(url) {
//                    pgLiveData.postValue(true)
//                }.collect {
//                    detailViewModel.what357(it)
//                }
//                return@launch
//            }
//            var s = url
//            if (!s.contains("dmzj"))
//                s = Api.url2 + "/" + s//需要加"/"
//            remote.getData(s) { pgLiveData.postValue(true) }
//                .collect {
//                    Log.i(TAG, "getHomeCartoon: $s")
//                    detailViewModel.what3(it)
//                }
//        }
//    }


    /**
     * 漫画本月人气排行
     */
//    fun getBanner() {
//        viewModelScope.launch(Dispatchers.IO) {
//            remote.getData(Api.url2 + "/rank/month-block-1.shtml")
//                .collect {
//                    what6(it)
//                }
//        }
//    }


    /**
     * 主页
     * homeFragment
     */

    //获取漫画详细
    fun getHomeCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = cartoonInfors[position]
        val s = info.href
        detailViewModel.putBundle(info.title, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        detailViewModel.loadCartoon(s)
    }

    //获取漫画页面
    private fun pager() =
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.url2 + "/update_$pager.shtml") {//需要加"/"
                errorLiveData.postValue("加载失败")
            }.collect {
                val document = Jsoup.parse(it)
                val element = document.getElementsByClass("newpic_content")
                val elements = element[0].getElementsByClass("boxdiv1")
                var element1: Element
                var element2: Element
                var element3: Element
                var cartoonInfor: CartoonInfor
                for (e in elements) {
                    element1 = e.select(".picborder a").first() //图片
                    element2 = e.select(".picborder img").first()
                    element3 = e.select(".pictext li")[2]
                    cartoonInfor = CartoonInfor(
                        element1.attr("title"),
                        element1.attr("href"),
                        element2.attr("src"),
                        element3.text()
                    )
                    cartoonInfors.add(cartoonInfor)
                }
                homeLiveData.postValue(true)
            }
        }


    //推荐
    fun get57Recommend() {
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.mh57Url)
                .collect {
                    val document = Jsoup.parse(it)
                    val element: Element = document.select(".update-wrap").first()
                    for (e in element.select("li")) {
                        val a = e.select("a").first()
                        val img = a.select("img").first()
                        var src = img.attr("src")
                        if (src.isEmpty()) src = img.attr("data-src")
                        homeRecommendList.add(
                            CartoonInfor(
                                a.attr("title"),
                                Api.mh57Url + a.attr("href"),
                                src
                            )
                        )
                    }
                    homeRecommendLiveData.postValue(homeRecommendList)
                    Log.i(TAG, "get57Recommend: $element")
                }
        }
    }

    //获取推荐漫画详细
    fun getHomeRecommendCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = homeRecommendList[position]
        val s = info.href
        Log.i(TAG, "getHomeRecommendCartoon: ${s + info.img}")
        detailViewModel.putBundle(info.title, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        detailViewModel.    loadCartoon(s)
    }

    fun nextPager() { //下一页
        pager++
        pager()
    }

    fun refreshPager() { //刷新
        pager = 1
        cartoonInfors.clear()
        pager()
    }

    fun getHomeCartoon() {
        pager()
    }




}
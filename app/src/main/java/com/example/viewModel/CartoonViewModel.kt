package com.example.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfo
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.example.util.RequestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

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

    //request
    private val requestUtil = RequestUtil
    val bundle
        get() = requestUtil.bundle
    val msg3LiveData
        get() = requestUtil.msg3LiveData

    //msg2主页漫画
    val cartoonInfors: MutableList<CartoonInfo> = ArrayList()
    val homeLiveData = MutableLiveData<Boolean>()
    private var pager = 1

    //主页57推荐漫画
    private val homeRecommendList = ArrayList<CartoonInfo>()
    val homeRecommendLiveData = MutableLiveData<List<CartoonInfo>>()


    //banner
//    val bannerList: MutableList<CartoonInfor> = ArrayList()
//    val bannerLiveData = MutableLiveData<List<CartoonInfor>>()
    val bannerList by lazy { arrayListOf(R.drawable.lzsm1, R.drawable.lzsy2, R.drawable.am3) }

    //remote
    private val remote = CartoonRemote
    val errorLiveData = remote.error

    //加载监听
    val pgLiveData = remote.pgLiveData
    val bottomLiveData = remote.bottomLiveData
    //
//    val bottomAlphaLiveData = MutableLiveData<Float>()

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
        requestUtil.putBundle(info.title, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        requestUtil.loadCartoon(s)
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
                var cartoonInfor: CartoonInfo
                for (e in elements) {
                    element1 = e.select(".picborder a").first() //图片
                    element2 = e.select(".picborder img").first()
                    element3 = e.select(".pictext li")[2]
                    cartoonInfor = CartoonInfo(
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

    //优酷漫画
    fun getYouKu() {
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData("https://www.ykmh.com/list/post/")
                .collect {
                    val document = Jsoup.parse(it)
                    val first = document.getElementsByClass("list_con_li clearfix").first()
                    Log.i(TAG, "getYouKu: $first")
                    for (element in first.select("li")) {
                        val a = element.select("a").first()
                        val img = a.select("img").first()
                        homeRecommendList.add(
                            CartoonInfo(
                                img.attr("alt"),
                                a.attr("href"),
                                img.attr("src")
                            )
                        )
                    }
                    homeRecommendLiveData.postValue(homeRecommendList)

                }
        }
    }

    //推荐
//    fun get57Recommend() {
//        viewModelScope.launch(Dispatchers.IO) {
//            remote.getData(Api.mh57Url)
//                .collect {
//                    val document = Jsoup.parse(it)
//                    val element: Element = document.select(".update-wrap").first()
//                    for (e in element.select("li")) {
//                        val a = e.select("a").first()
//                        val img = a.select("img").first()
//                        var src = img.attr("src")
//                        if (src.isEmpty()) src = img.attr("data-src")
//                        homeRecommendList.add(
//                            CartoonInfo(
//                                a.attr("title"),
//                                Api.mh57Url + a.attr("href"),
//                                src
//                            )
//                        )
//                    }
//                    homeRecommendLiveData.postValue(homeRecommendList)
//                    Log.i(TAG, "get57Recommend: $element")
//                }
//        }
//    }

    //获取推荐漫画详细
//    fun getHomeRecommendCartoon(position: Int) {
//        if (pgLiveData.value == false) return
//        pgLiveData.value = false
//        val info = homeRecommendList[position]
//        val s = info.href
//        Log.i(TAG, "getHomeRecommendCartoon: ${s + info.img}")
//        requestUtil.putBundle(info.title, info.img, s, R.id.homeFragment)
//        if (s.isEmpty()) {
//            pgLiveData.value = true
//            return
//        }
//        requestUtil.loadCartoon(s)
//    }
    //获取优酷漫画详细
    fun getHomeYouKuCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = homeRecommendList[position]
        val s = info.href
        Log.i(TAG, "getHomeRecommendCartoon: ${s + info.img}")
        requestUtil.putBundle(info.title, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        requestUtil.loadCartoon(s)
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
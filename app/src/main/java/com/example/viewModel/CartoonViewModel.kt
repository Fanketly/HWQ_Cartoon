package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
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

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 20:35
 */

class CartoonViewModel @ViewModelInject constructor(
    private val requestUtil: RequestUtil,
    private val remote: CartoonRemote
) : ViewModel() {
    init {
        Log.i("CREATE", "CartoonViewModel_: ")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("TAG","CartoonViewModel_onCleared: ")
    }
    //跳转到Detail监听
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
    //val bannerList: MutableList<CartoonInfor> = ArrayList()
    //val bannerLiveData = MutableLiveData<List<CartoonInfor>>()
    val bannerList by lazy { arrayListOf(R.drawable.lzsm1, R.drawable.lzsy2, R.drawable.am3) }

    //轮播图监听
    val lbtLiveData = MutableLiveData<Boolean>()

    //错误
    val errorLiveData = remote.error

    //加载监听
    val pgLiveData = remote.pgLiveData
    val bottomLiveData = remote.bottomLiveData

    //val bottomAlphaLiveData = MutableLiveData<Float>()
    //获取漫画详细
    fun getHomeCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = cartoonInfors[position]
        val s = info.href
        Log.i("TAG", "CartoonViewModel_getHomeCartoon 存进去的地址:$s ")
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
            remote.getData<CartoonInfo>(Api.url2 + "/update_$pager.shtml",
                data = { data, flow ->
                    for (it in Jsoup.parse(data)
                        .getElementsByClass("newpic_content")[0].getElementsByClass("boxdiv1")) {
                        val element1 = it.select(".picborder a").first() //图片
                        flow.emit(
                            CartoonInfo(
                                element1.attr("title"),
                                element1.attr("href"),
                                it.select(".picborder img").first().attr("src"),
                                it.select(".pictext li")[2].text()
                            )
                        )
                    }
                },
                success = {
                    homeLiveData.postValue(true)
                },
                fail = {
                    homeLiveData.postValue(true)
                }
            ).collect {
                cartoonInfors.add(it)
            }
        }


    //优酷漫画
    fun getYouKu() {
        viewModelScope.launch(Dispatchers.IO) {
            if (homeRecommendList.size > 0) homeRecommendList.clear()
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


    //获取优酷漫画详细
    fun getHomeYouKuCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = homeRecommendList[position]
        val s = info.href
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

    fun getHomeCartoon() = pager()


}
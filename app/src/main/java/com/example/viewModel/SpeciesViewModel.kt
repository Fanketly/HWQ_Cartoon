package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.SpeciesInfo
import com.example.repository.model.SpeciesInfo2
import com.example.repository.model.SpeciesInfo2Item
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.example.util.RequestUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/5
 * Time: 17:04
 */
class SpeciesViewModel @ViewModelInject constructor(
    private val requestUtil: RequestUtil,
    private val remote: CartoonRemote
) : ViewModel() {
    private val speciesList: MutableList<FavouriteInfor> by lazy { ArrayList() }
    private val typeList: MutableList<SpeciesInfo> by lazy { ArrayList() }
    val typesList
        get() = typeList
    val speciesLiveData by lazy { MutableLiveData<List<FavouriteInfor>>() }

    //    private val remote = CartoonRemote
    private val pgLiveData = remote.pgLiveData
    private val errorLiveData = remote.error

    //request
//    private val requestUtil = RequestUtil

    //加载分类
    fun getSpeciesType() {
        if (typeList.size > 0) {//判断是否有加载过分类，有就加载现有数据
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.url2 + "/tags/category_search/0-0-0-all-0-0-0-1.shtml#category_nav_anchor")
                .collect {
                    what8(it)
                }
        }
    }

    val adapterTopLiveData = MutableLiveData<Boolean>()//分类切换监听有没有成功 成功就变色

    //初始化加载全部分类数据；加载切换分类数据；加载刷新数据
    fun getSpeciesData(species: String, p: Int) {
        if (speciesList.size > 0) speciesList.clear()
        Log.i(TAG, "getSpecies:$species")
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(
                //&_order=h 排序 p=1页数
                Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=$p&callback=search.renderResult"
            )
                .collect {
                    what7(it)
                }
        }
    }

    //加载更多
    fun loadMoreData(species: String, p: Int) {
        Log.i(TAG, "loadMoreData:$p ")
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(
                //&_order=h 排序 p=1页数
                Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=$p&callback=search.renderResult"
            )
                .collect {
                    what7(it)
                }
        }
    }

    //加载所点击漫画数据
    fun getSpeciesCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = speciesList[position]
        val s = info.url
        requestUtil.putBundle(info.title, info.imgUrl, s, R.id.speciesFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        requestUtil.loadCartoon(s)
    }

    //解析数据
    private fun what7(str2: String) {
        Log.i(TAG, "what7: $str2")
        if (!str2.contains("search.renderResult")) {
            errorLiveData.postValue("数据加载失败")
            return
        }
        adapterTopLiveData.postValue(true)
        val str = str2.substring(str2.indexOf("["), str2.lastIndexOf("]") + 1)
        val gson = Gson()
        val speciesInfor2s: List<SpeciesInfo2Item> =
            gson.fromJson(
                str,
                SpeciesInfo2::class.java
            )
        Log.i(TAG, ": " + speciesInfor2s[0].comic_cover)
        for ((_, comic_cover, comic_url, _, _, _, _, name) in speciesInfor2s) {
            speciesList.add(
                FavouriteInfor(
                    comic_url,
                    "https:$comic_cover",
                    name
                )
            )
        }
        speciesLiveData.postValue(speciesList)
    }

    //解析分类数据
    private fun what8(s: String) {
        typeList.add(SpeciesInfo("0", "全部"))
        val document = Jsoup.parse(s)
        val typeElms = document.getElementsByClass("search_list_m_right")[4]
        for (a in typeElms.select("a")) {
            typeList.add(
                SpeciesInfo(
                    a.attr("id").substring(5),
                    a.attr("title")
                )
            )
        }
        //解析完加载全部分类的漫画
        getSpeciesData("0", 1)
    }

}
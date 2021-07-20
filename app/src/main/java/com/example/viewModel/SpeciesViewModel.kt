package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.TAG
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.SpeciesInfo
import com.example.repository.model.SpeciesInfo2
import com.example.repository.model.SpeciesInfo2Item
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
    val speciesLiveData = MutableLiveData<List<FavouriteInfor>>()

    //    private val remote = CartoonRemote
    private val pgLiveData = remote.pgLiveData
    private val errorLiveData = remote.error

    //记录页数
    private var pager = 1

    //当前分类类型
    private var species = "0"

    //分类切换监听有没有成功 成功就变色
    val adapterTopLiveData = MutableLiveData<Boolean>()


    //加载分类
    fun getSpeciesType() {
        if (typeList.size > 0) {//判断是否有加载过分类，有就加载现有数据
            what7(Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=1&callback=search.renderResult")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData<SpeciesInfo>(Api.url2 + "/tags/category_search/0-0-0-all-0-0-0-1.shtml#category_nav_anchor",
                data = { data, flow ->
                    typeList.add(SpeciesInfo("0", "全部"))
                    val document = Jsoup.parse(data)
                    val typeElms = document.getElementsByClass("search_list_m_right")[4]
                    for (a in typeElms.select("a")) {
                        flow.emit(
                            SpeciesInfo(
                                a.attr("id").substring(5),
                                a.attr("title")
                            )
                        )
                    }
                }, success = {
                    //解析完加载全部分类的漫画
                    what7(Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=1&callback=search.renderResult")

                }).collect {
                typeList.add(it)
            }
        }
    }


    //加载当前分类漫画
    fun refresh() {
        if (speciesList.size > 0) speciesList.clear()
        what7(Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=1&callback=search.renderResult")

    }

    //切换分类
    fun switchCategory(id: String): Boolean {
        if (species == id) return false
        species = id
        if (speciesList.size > 0) speciesList.clear()
        pager = 1
        Log.i(TAG, "switchCategory:$species")
        what7(Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=1&callback=search.renderResult")
        adapterTopLiveData.postValue(true)
        return true
    }

    //加载更多
    fun loadMoreData() {
        pager += 1
        Log.i(TAG, "loadMoreData:$pager ")
        what7(
            Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&_order=h&p=$pager&callback=search.renderResult"
        )
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

    /**
     * &_order=h 排序 p=1页数
     * 解析数据
     * **/
    private fun what7(url: String) =
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData<FavouriteInfor>(url, data = { str2, flow ->
                Log.i(TAG, "what7: $str2")
                if (!str2.contains("search.renderResult")) {
                    errorLiveData.postValue("数据加载失败")
                } else {
                    val str = str2.substring(str2.indexOf("["), str2.lastIndexOf("]") + 1)
                    val speciesInfor2s: List<SpeciesInfo2Item> =
                        remote.gson.fromJson(
                            str,
                            SpeciesInfo2::class.java
                        )
                    Log.i(TAG, ": " + speciesInfor2s[0].comic_cover)
                    for ((_, comic_cover, comic_url, _, _, _, _, name) in speciesInfor2s) {
                        flow.emit(
                            FavouriteInfor(
                                comic_url,
                                "https:$comic_cover",
                                name
                            )
                        )
                    }
                }
            }, success = { speciesLiveData.postValue(speciesList) }).collect { speciesList.add(it) }
        }


}
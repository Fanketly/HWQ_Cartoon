package com.example.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.local.CartoonDB
import com.example.repository.local.HistoryDB
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/15
 * Time: 20:32
 * 历史和喜爱数据库
 */
class FavouriteViewModel : ViewModel() {
    private val historyDB = HistoryDB()
    private val favouriteDB = CartoonDB()
    val historyList: MutableList<HistoryInfor> = mutableListOf()
    val favouriteList: MutableList<FavouriteInfor> = mutableListOf()
    val likesLiveData = MutableLiveData<Boolean>()
    lateinit var detailViewModel: DetailViewModel

    /**判断是删除还是添加，方便rv刷新**/
    var delOrIns = true
    val historyLivaData = MutableLiveData<Int>()
    val favouriteLivaData = MutableLiveData<Int>()

    //remote
    private val remote = CartoonRemote

    //监听是否隐藏bottom false为显示

    //加载监听
    private val pgLiveData = remote.pgLiveData

    init {
        historyList.addAll(historyDB.loadAll())
        favouriteList.addAll(favouriteDB.loadAll())
    }


    /***
     * favouriteFragment
     *
     */
    fun favouriteGet(favouriteInfor: FavouriteInfor) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = favouriteInfor.url
        Log.i(TAG, "favouriteGet: $url")
        detailViewModel.putBundle(
            favouriteInfor.title,
            favouriteInfor.imgUrl,
            url,
            R.id.favoriteFragment
        )
        detailViewModel.loadCartoon(url)

    }

    /**
     *historyFragment
     * */
    fun historyGet(historyInfor: HistoryInfor) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = historyInfor.href
        Log.i(TAG, "historyGet: $url")
        detailViewModel.putBundle(
            historyInfor.title,
            historyInfor.imgUrl,
            url,
            R.id.historyFragment
        )

        detailViewModel.loadCartoon(url)

    }


    fun likesIsZero() {
        if (favouriteList.size == 0)
            likesLiveData.postValue(true)
        else
            likesLiveData.postValue(false)
    }


    fun historyInsert(historyInfor: HistoryInfor) {
        historyDB.insert(historyInfor)
    }


    fun historyUpdate(historyInfor: HistoryInfor) {
        historyDB.update(historyInfor)
    }

    fun historyClear() {
        historyDB.clear()
        historyList.clear()
        historyLivaData.value = -1//全部刷新
    }

    //homeFragment
    fun setFavouriteFromHome(historyInfor: HistoryInfor): FavouriteInfor {
        val s = historyInfor.href
        val favouriteInfor: FavouriteInfor = if (s.contains("dmzj")) {
            FavouriteInfor(s, historyInfor.imgUrl, historyInfor.title)
        } else {
            FavouriteInfor(
                historyInfor.mark,
                Api.url2 + "/" + s,
                historyInfor.imgUrl,
                historyInfor.title
            )
        }
        favouriteDB.insert(favouriteInfor)
        return favouriteInfor
    }

    //favouriteFragment
    fun setFavourite(favourite: FavouriteInfor?) {
        favouriteDB.insert(favourite)
    }

    fun favouriteDel(favouriteMark: Int) {
        delOrIns = true
        favouriteLivaData.value = favouriteMark
        favouriteDB.del(favouriteList.removeAt(favouriteMark))
    }

    fun updateFavourite(favouriteInfor: FavouriteInfor?) {
        favouriteDB.updata(favouriteInfor)
    }

    fun favouriteListAdd(favouriteInfor: FavouriteInfor): Int {
        favouriteList.add(favouriteInfor)
        delOrIns = false
        val p = favouriteList.size - 1
        favouriteLivaData.value = p
        return p
    }


}
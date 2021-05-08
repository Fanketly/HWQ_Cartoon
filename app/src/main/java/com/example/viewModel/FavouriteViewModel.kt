package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.local.CartoonDB
import com.example.repository.local.HistoryDB
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.repository.remote.CartoonRemote
import com.example.util.RequestUtil

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/15
 * Time: 20:32
 * 历史和喜爱数据库
 */
class FavouriteViewModel @ViewModelInject constructor(
    private val requestUtil: RequestUtil,
    remote: CartoonRemote,
    private val historyDB: HistoryDB,
    private val favouriteDB: CartoonDB
) : ViewModel() {

    val historyList: MutableList<HistoryInfor> = mutableListOf()
    val favouriteList: MutableList<FavouriteInfor> = mutableListOf()
    val likesLiveData = MutableLiveData<Boolean>()

    /**判断是删除还是添加，方便rv刷新**/
    var delOrIns = true
    val historyLivaData = MutableLiveData<Int>()
    val favouriteLivaData = MutableLiveData<Int>()

    //加载监听
    private val pgLiveData = remote.pgLiveData

    init {
        historyList.addAll(historyDB.loadAll().reversed())
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
        requestUtil.putBundle(
            favouriteInfor.title,
            favouriteInfor.imgUrl,
            url,
            R.id.favoriteFragment
        )
        requestUtil.loadCartoon(url)
    }

    /**
     *historyFragment
     * */
    fun historyGet(it: Int) {
        val historyInfor = historyList[it]
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = historyInfor.href
        Log.i(TAG, "historyGet: $url")
        requestUtil.putBundle(
            historyInfor.title,
            historyInfor.imgUrl,
            url,
            R.id.historyFragment
        )

        requestUtil.loadCartoon(url)

    }

    /**
     * 判断追漫是否为0
     * **/
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
    fun setFavouriteFromHome(historyMark: Int): FavouriteInfor {
        val historyInfo = historyList[historyMark]
        val s = historyInfo.href
        Log.i("TAG", "FavouriteViewModel_setFavouriteFromHome 即将添加进喜爱的地址:$s ")
        val favouriteInfo =
            FavouriteInfor(
                historyInfo.mark,
                s,
                historyInfo.imgUrl,
                historyInfo.title
            )
//            FavouriteInfor(
//                historyInfor.mark,
//                Api.url2 + "/" + s,
//                historyInfor.imgUrl,
//                historyInfor.title
        favouriteDB.insert(favouriteInfo)
        return favouriteInfo
    }

    //favouriteFragment
    fun setFavourite(favourite: FavouriteInfor?) {
        favouriteDB.insert(favourite)
    }

    fun favouriteDel(favouriteMark: Int) {
        delOrIns = true
        favouriteDB.del(favouriteList.removeAt(favouriteMark))
        favouriteLivaData.value = favouriteMark
    }

    fun favouriteUpdate(favouriteInfor: FavouriteInfor?) {
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
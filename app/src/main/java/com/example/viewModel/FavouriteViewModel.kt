package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.StateEnum
import com.example.repository.local.CartoonDB
import com.example.repository.local.HistoryDB
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.repository.remote.CartoonRemote
import com.example.repository.remote.RequestUtil

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

    val historyList: MutableList<HistoryInfor> = ArrayList()
    private val _favouriteList: MutableList<FavouriteInfor> = ArrayList()
    val favouriteList: List<FavouriteInfor>
        get() = _favouriteList
    private val _likesLiveData = MutableLiveData<Boolean>()
    val likesLiveData: LiveData<Boolean>
        get() = _likesLiveData

    /**判断是删除还是添加，方便rv刷新**/
    private var _delOrIns = true
    val delOrIns
        get() = _delOrIns
    val historyLivaData = MutableLiveData<Int>()
    val favouriteLivaData = MutableLiveData<Int>()

    //加载监听
    private val pgLiveData = remote.pgLiveData

    //数量
    private var _historySize = 0
    val historySize
        get() = _historySize
    private var _favouriteSize = 0
    val favouriteSize
        get() = _favouriteSize
    private val sizeLive = MutableLiveData<StateEnum>()
    val sizeLiveData: LiveData<StateEnum>
        get() = sizeLive

    init {
        this.historyList.addAll(historyDB.loadAll().reversed())
        _favouriteList.addAll(favouriteDB.loadAll())
    }

    /**
     * 获取数量
     * */
    fun getSize() {
        _historySize = this.historyList.size
        _favouriteSize = _favouriteList.size
        sizeLive.postValue(StateEnum.SUCCESS)
    }

    fun onDestroyMeView() {
        sizeLive.value = StateEnum.NOTHING
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
        val historyInfo = this.historyList[it]
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = historyInfo.href
        Log.i(TAG, "historyGet: $url")
        requestUtil.putBundle(
            historyInfo.title,
            historyInfo.imgUrl,
            url,
            R.id.historyFragment
        )

        requestUtil.loadCartoon(url)

    }

    /**
     * 判断追漫是否为0
     * **/
    fun likesIsZero() {
        if (_favouriteList.isEmpty())
            _likesLiveData.postValue(true)
        else
            _likesLiveData.postValue(false)
    }


    fun historyInsert(historyInfor: HistoryInfor) {
        historyDB.insert(historyInfor)
    }


    fun historyUpdate(historyInfor: HistoryInfor) {
        historyDB.update(historyInfor)
    }

    fun historyClear() {
        historyDB.clear()
        this.historyList.clear()
        historyLivaData.value = -1//全部刷新
    }

    //homeFragment
    fun setFavouriteFromHome(historyMark: Int): FavouriteInfor {
        val historyInfo = this.historyList[historyMark]
        val s = historyInfo.href
        Log.i("TAG", "FavouriteViewModel_setFavouriteFromHome 即将添加进喜爱的地址:$s ")
        val favouriteInfo =
            FavouriteInfor(
                historyInfo.mark,
                s,
                historyInfo.imgUrl,
                historyInfo.title
            )
        favouriteDB.insert(favouriteInfo)
        return favouriteInfo
    }

    //favouriteFragment
    fun setFavourite(favourite: FavouriteInfor?) {
        favouriteDB.insert(favourite)
    }

    fun favouriteDel(favouriteMark: Int) {
        _delOrIns = true
        favouriteDB.del(_favouriteList.removeAt(favouriteMark))
        favouriteLivaData.value = favouriteMark
    }

    fun favouriteUpdate(favouriteInfor: FavouriteInfor?) {
        favouriteDB.updata(favouriteInfor)
    }

    fun favouriteListAdd(favouriteInfo: FavouriteInfor): Int {
        _favouriteList.add(favouriteInfo)
        _delOrIns = false
        val p = _favouriteList.size - 1
        favouriteLivaData.value = p
        return p
    }


}
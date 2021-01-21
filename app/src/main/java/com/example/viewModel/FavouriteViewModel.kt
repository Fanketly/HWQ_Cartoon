package com.example.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.repository.local.CartoonDB
import com.example.repository.local.HistoryDB
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.HistoryInfor
import com.example.repository.remote.Api

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/15
 * Time: 20:32
 * 历史和喜爱数据库
 */
class FavouriteViewModel : ViewModel() {
    val tabLayLiveData = MutableLiveData<Boolean>()
    private val historyDB = HistoryDB()
    private val favouriteDB = CartoonDB()
    val historyList: MutableList<HistoryInfor> = mutableListOf()
    val favouriteList: MutableList<FavouriteInfor> = mutableListOf()
    var delOrIns = true//判断是删除还是添加，方便rv刷新
    val historyLivaData = MutableLiveData<Int>()
    val favouriteLivaData = MutableLiveData<Int>()

    init {
        historyList.addAll(historyDB.loadAll())
        favouriteList.addAll(favouriteDB.loadAll())
    }

    fun historyInsert(historyInfor: HistoryInfor) {
        historyDB.insert(historyInfor)
    }


    fun historyUpdate(historyInfor: HistoryInfor) {
        historyDB.update(historyInfor)
    }

    //homeFragment
    fun setFavouriteFromHome(historyInfor: HistoryInfor): FavouriteInfor {
        val s = historyInfor.href
        val favouriteInfor: FavouriteInfor
        favouriteInfor = if (s.contains("dmzj")) {
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
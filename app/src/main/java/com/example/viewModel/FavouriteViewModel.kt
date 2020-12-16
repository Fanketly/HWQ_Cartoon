package com.example.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.repository.local.HistoryDB
import com.example.repository.model.HistoryInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/15
 * Time: 20:32
 */
class FavouriteViewModel : ViewModel() {
    val tabLayLiveData = MutableLiveData<Boolean>()
    private val db = HistoryDB()
    val historyList:MutableList<HistoryInfor> = mutableListOf()
    val historyLivaData=MutableLiveData<Int>()
    init {
        historyList.addAll(db.loadAll())
    }
    fun insert(historyInfor: HistoryInfor) {
        db.insert(historyInfor)
    }


    fun update(historyInfor: HistoryInfor) {
        db.update(historyInfor)
    }
}
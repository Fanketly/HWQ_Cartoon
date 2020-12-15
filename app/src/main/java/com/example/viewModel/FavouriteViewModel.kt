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
class FavouriteViewModel :ViewModel(){
    val tabLayLiveData=MutableLiveData<Boolean>()
    private val db = HistoryDB()
    fun insert(historyInfor: HistoryInfor) {
        db.insert(historyInfor)
    }

    fun getAll(): List<HistoryInfor> {
        return db.loadAll()
    }
    fun update(historyInfor: HistoryInfor){
        db.update(historyInfor)
    }
}
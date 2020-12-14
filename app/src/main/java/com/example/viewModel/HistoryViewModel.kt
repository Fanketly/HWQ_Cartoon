package com.example.viewModel

import androidx.lifecycle.ViewModel
import com.example.repository.local.HistoryDB
import com.example.repository.model.HistoryInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:16
 */
class HistoryViewModel : ViewModel() {
    private val db = HistoryDB()
    fun insert(historyInfor: HistoryInfor) {
        db.insert(historyInfor)
    }

    fun getAll(): List<HistoryInfor> {
        return db.loadAll()
    }

}
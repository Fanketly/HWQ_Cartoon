package com.example.repository.local

import com.example.hwq_cartoon.App
import com.example.repository.model.HistoryInfor
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:16
 */
@ActivityScoped
class HistoryDB @Inject constructor() {
    private val historyInfoDao = App.historySession.historyInforDao
    fun insert(historyInfo: HistoryInfor) {
        historyInfoDao.insert(historyInfo)
    }

    fun loadAll(): List<HistoryInfor> {
        return historyInfoDao.loadAll()
    }

    fun update(historyInfo: HistoryInfor) = historyInfoDao.update(historyInfo)
    fun clear() = historyInfoDao.deleteAll()
}
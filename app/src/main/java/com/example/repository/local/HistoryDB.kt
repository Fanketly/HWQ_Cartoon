package com.example.repository.local

import com.example.hwq_cartoon.App
import com.example.repository.model.HistoryInfor

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:16
 */
class HistoryDB {
    private val historyInfoDao = App.historySession.historyInforDao
    fun insert(historyInfor: HistoryInfor) {
        historyInfoDao.insert(historyInfor)
    }

    fun loadAll(): List<HistoryInfor> {
        return historyInfoDao.loadAll()
    }

    fun update(historyInfor: HistoryInfor) = historyInfoDao.update(historyInfor)
    fun clear() = historyInfoDao.deleteAll()
}
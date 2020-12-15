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
    private val historyInforDao = App.historySession.historyInforDao
    fun insert(historyInfor: HistoryInfor) {
        historyInforDao.insert(historyInfor)
    }

    fun loadAll(): List<HistoryInfor> {
        return historyInforDao.loadAll()
    }
    fun close() {
        App.historySession.database.close()
        App.historySession.clear()
    }
fun update(historyInfor: HistoryInfor)=historyInforDao.update(historyInfor)
}
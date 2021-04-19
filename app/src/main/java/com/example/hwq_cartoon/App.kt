package com.example.hwq_cartoon

import android.app.Application
import com.example.base.AUTO
import com.example.base.dataStore
import com.example.repository.model.DaoMaster
import com.example.repository.model.DaoSession
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.map

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:36
 */

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var favouriteSession: DaoSession
        lateinit var historySession: DaoSession
        var autoSetting: Int = 200
    }

    override fun onCreate() {
        super.onCreate()
        historySession = daoMaster("history.db")
        favouriteSession = daoMaster("favourite.db")
        applicationContext.dataStore.data.map {
            autoSetting = it[AUTO] ?: 200
        }
    }

    private fun daoMaster(string: String): DaoSession {
        val devOpenHelper = DaoMaster.DevOpenHelper(applicationContext, string)
        val database = devOpenHelper.writableDatabase
        return DaoMaster(database).newSession()
    }
}
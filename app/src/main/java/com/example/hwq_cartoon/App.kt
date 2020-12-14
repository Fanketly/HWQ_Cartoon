package com.example.hwq_cartoon

import android.app.Application
import com.example.repository.model.DaoMaster
import com.example.repository.model.DaoSession

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:36
 */
class App : Application() {
    companion object {
        lateinit var favouriteSession: DaoSession
        lateinit var historySession: DaoSession
    }

    override fun onCreate() {
        super.onCreate()
        historySession = daomaster("history.db")
        favouriteSession = daomaster("favourite.db")
    }

    private fun daomaster(string: String): DaoSession {
        val devOpenHelper = DaoMaster.DevOpenHelper(applicationContext, string)
        val database = devOpenHelper.writableDatabase
        return DaoMaster(database).newSession()
    }
}
package com.example.hwq_cartoon

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.base.AUTO
import com.example.base.dataStore
import com.example.repository.model.DaoMaster
import com.example.repository.model.DaoSession
import com.example.util.NetworkUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.map

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/13
 * Time: 22:36
 */
@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    companion object {
        lateinit var favouriteSession: DaoSession
        lateinit var historySession: DaoSession
        var autoSetting: Int = 200
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
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

    /**
     * 不使用默认,自己建一个单例,ImageLoaderFactory自动延迟初始化
     * memoryCachePolicy 设置默认的内存缓存策略
     * **/
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .okHttpClient {
                NetworkUtils.okHttpClient
            }
//            .memoryCachePolicy(CachePolicy.DISABLED)
            .build()
    }
}
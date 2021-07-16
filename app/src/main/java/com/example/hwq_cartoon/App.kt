package com.example.hwq_cartoon

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.repository.model.DaoMaster
import com.example.repository.model.DaoSession
import com.example.util.NetworkUtils
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

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
        var autoSetting: Int? = null
        var blackTheme: Boolean = false
        var pagerOrientation: Int? = null
        lateinit var appContext: Context
        lateinit var kv: MMKV
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("TAG", "App_onCreate: ")
        appContext = applicationContext
        MMKV.initialize(this)
        kv = MMKV.defaultMMKV()!!
        kv.apply {
            blackTheme = decodeBool(THEME, false)
            autoSetting = decodeInt(AUTO, 200)
            pagerOrientation = decodeInt(PAGER_ORIENTATION, LinearLayout.VERTICAL)
        }
        historySession = daoMaster("history.db")
        favouriteSession = daoMaster("favourite.db")
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
        return ImageLoader.Builder(applicationContext).apply {
            okHttpClient(NetworkUtils.okHttpClient)
            placeholder(R.drawable.place)
            error(R.drawable.error)
        }.build()
    }
}
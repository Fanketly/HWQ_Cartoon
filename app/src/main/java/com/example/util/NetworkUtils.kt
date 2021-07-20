package com.example.util

import coil.util.CoilUtils
import com.example.hwq_cartoon.App
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 21:22
 */
object NetworkUtils {
    //cache(CoilUtils.createDefaultCache(App.appContext)) 设置了才能进行本地缓存
    private val client = OkHttpClient.Builder()
        .callTimeout(4, TimeUnit.SECONDS)
        .cache(CoilUtils.createDefaultCache(App.appContext))
        .build()
    val okHttpClient
        get() = client

    fun okhttpGet(url: String): String {
        return client.newCall(Request.Builder().get().url(url).build()).execute().body?.string()
            ?: return ""
    }

    fun okhttpGet(url: String, headers: Headers): String {
        return client.newCall(Request.Builder().headers(headers).get().url(url).build())
            .execute().body?.string() ?: return ""
    }
}
package com.example.util

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 21:22
 */
object NetworkUtils {

    private val okHttpClient = OkHttpClient()
    fun okhttpGet(url: String): String {
        val request: Request = Request.Builder().get().url(url).build()
        val call = okHttpClient.newCall(request)
        return call.execute().body?.string() ?: return ""
    }


    fun send(url: String): ByteArray? {
        Log.i("TAG", "send: $url")
        val request: Request = Request.Builder()
            .url(url)
            .addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36"
            )
            .addHeader("Referer", "https://manhua.dmzj.com/tongzhenmiejueliedao/99325.shtml")
            .build()
        val call = okHttpClient.newCall(request)
        return call.execute().body?.bytes()
    }
}
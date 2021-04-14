package com.example.util

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

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(4, TimeUnit.SECONDS)
        .build()

    fun okhttpGet(url: String): String {
        val request: Request = Request.Builder().get().url(url).build()
        val call = okHttpClient.newCall(request)
        return call.execute().body?.string() ?: return ""
    }


//    fun send(url: String): ByteArray? {
//        Log.i("TAG", "send: $url")
//        val request: Request = Request.Builder()
//            .url(url)
//            .build()
//        val call = okHttpClient.newCall(request)
//        return call.execute().body?.bytes()
//    }
}
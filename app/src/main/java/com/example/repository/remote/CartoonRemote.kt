package com.example.repository.remote

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.util.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import okhttp3.Headers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 19:18
 */

@Singleton
class CartoonRemote @Inject constructor() {
    private val errorLiveData = MutableLiveData<String?>()
    private val pg = MutableLiveData<Boolean>()
    private val bottom = MutableLiveData<Boolean>()
    private val _gson = Gson()
    val gson
        get() = _gson
    val bottomLiveData
        get() = bottom
    val pgLiveData
        get() = pg
    val error
        get() = errorLiveData

    //请求api时使用
    @WorkerThread
    fun <T> getData(url: String, clazz: Class<T>, headers: Headers? = null): T? {
        try {
            val okhttpGet = if (headers == null) NetworkUtils.okhttpGet(url)
            else NetworkUtils.okhttpGet(url, headers)
            return _gson.fromJson(okhttpGet, clazz)
        } catch (e: Exception) {
            pg.postValue(true)
            errorLiveData.postValue(e.message)
        }
        return null
    }


    //挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是Kotlin流（Flow）的⽤武之地
    //onCompletion 的主要优点是其 lambda 表达式的可空参数 Throwable 可以⽤于确定流收集是正常完成还是有异 常发⽣
    //onCompletion 操作符与 catch 不同，它不处理异常。我们可以看到前⾯的⽰例代码，异常仍然流向下游。它将被提供 给后⾯的 onCompletion 操作符，并可以由 catch 操作符处理。
    /**
     * flow用法
     * 爬取网页数据时使用
     * @param url 加载数据的地址
     * @param data 发射的数据
     * @param success 当成功时做的一些事情
     * @param fail 当成功时做的一些事情
     * **/
    @WorkerThread
    suspend fun <T> getData(
        url: String,
        data: suspend (data: String, flow: FlowCollector<T>) -> Unit,
        success: (suspend () -> Unit)? = null,
        fail: (suspend () -> Unit)? = null
    ) = flow {
        data(NetworkUtils.okhttpGet(url), this)
    }.onCompletion { cause -> if (cause == null) success?.invoke() }
        .catch {
            errorLiveData.postValue(it.message)
            pg.postValue(true)
            fail?.invoke()
        }


}
package com.example.repository.remote

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.util.NetworkUtils
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 19:18
 */


object CartoonRemote {
    private val errorLiveData = MutableLiveData<String>()
    private val pg = MutableLiveData<Boolean>()
    private val bottom = MutableLiveData<Boolean>()

    val bottomLiveData
        get() = bottom
    val pgLiveData
        get() = pg
    val error
        get() = errorLiveData

    //建议多个数据使用flow,这个不是正确用法
    //挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是Kotlin流（Flow）的⽤武之地
    @WorkerThread
    suspend fun getData(url: String) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
        pg.postValue(true)
    }

    /**
     * flow用法
     * @param success 当成功时做的一些事情
     * @param data 发射的数据
     * **/
    @WorkerThread
    suspend fun <T> getData(
        url: String,
        data: suspend (data: String, flow: FlowCollector<T>) -> Unit,
        success: () -> Unit
    ) = flow {
        data(NetworkUtils.okhttpGet(url), this)
    }.onCompletion { cause -> if (cause == null) success() }
        .catch {
            error.postValue(it.message)
            pg.postValue(true)
        }



    /**
     * @param fail 当错误时做的一些事情
     * **/
    @WorkerThread
    suspend fun getData(url: String, fail: () -> Unit) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
        pg.postValue(true)
        fail()
    }

}
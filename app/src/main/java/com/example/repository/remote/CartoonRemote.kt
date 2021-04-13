package com.example.repository.remote

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.util.NetworkUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

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
//只是为了练flow使用，建议多个数据再使用flow
    @WorkerThread
    suspend fun getData(url: String) =  flow{
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
    }
    //只是为了练flow使用，建议多个数据再使用flow
    //挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是Kotlin流（Flow）的⽤武之地
    @WorkerThread
    suspend fun getData(url: String, doAny: () -> Unit) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
        doAny()
    }

}
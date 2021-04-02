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

    @WorkerThread
    suspend fun getData(url: String) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
    }

    @WorkerThread
    suspend fun getData(url: String, doAny: () -> Unit) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch {
        error.postValue(it.message)
        doAny()
    }

    @WorkerThread
    suspend fun getImg(url: String) = flow { emit(NetworkUtils.send(url)) }
        .catch { error.postValue(it.message) }

}
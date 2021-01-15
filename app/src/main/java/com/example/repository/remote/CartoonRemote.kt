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


class CartoonRemote(private val error: MutableLiveData<String>) {
    @WorkerThread
    suspend fun getData(url: String) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch { error.postValue(it.message) }


    @WorkerThread
    suspend fun getImg(url: String) = flow { emit(NetworkUtils.send(url)) }
        .catch { error.postValue(it.message) }

}
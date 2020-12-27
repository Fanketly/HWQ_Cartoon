package com.example.repository.remote

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 19:18
 */
@ExperimentalCoroutinesApi
class CartoonRemote(private val error:MutableLiveData<String>) {
    @WorkerThread
    suspend fun getData(url: String) = flow {
        emit(NetworkUtils.okhttpGet(url))
    }.catch { error.postValue(it.message) }.flowOn(Dispatchers.IO)

    @WorkerThread
    suspend fun getImg(url: String) = flow { emit(NetworkUtils.send(url)) }
        .catch { error.postValue(it.message) }
        .flowOn(Dispatchers.IO)
}
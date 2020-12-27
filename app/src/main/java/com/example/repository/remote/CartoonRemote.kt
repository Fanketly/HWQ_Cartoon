package com.example.repository.remote

import com.example.util.NetworkUtils
import kotlinx.coroutines.flow.flow

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 19:18
 */
class CartoonRemote {

     fun getData(url: String)= flow{
                emit( NetworkUtils.okhttpGet(url)) }


}
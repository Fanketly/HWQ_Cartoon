package com.example.repository.local

import com.example.hwq_cartoon.App
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.FavouriteInforDao
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 22:11
 */
@ActivityScoped
class CartoonDB @Inject constructor(){
    private val favouriteInfoDao: FavouriteInforDao = App.favouriteSession.favouriteInforDao


    fun loadAll(): List<FavouriteInfor> {
        return favouriteInfoDao.loadAll()
    }

    fun insert(favouriteInfor: FavouriteInfor?) {
        favouriteInfoDao.insert(favouriteInfor)
    }


    fun del(favouriteInfor: FavouriteInfor?) {
        favouriteInfoDao.delete(favouriteInfor)
    }

    fun update(favouriteInfor: FavouriteInfor?) {
        favouriteInfoDao.update(favouriteInfor)
    }
}
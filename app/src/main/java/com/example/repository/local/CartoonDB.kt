package com.example.repository.local

import com.example.hwq_cartoon.App
import com.example.repository.model.FavouriteInfor
import com.example.repository.model.FavouriteInforDao

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/2/10
 * Time: 22:11
 */
class CartoonDB {
    private val favouriteInforDao: FavouriteInforDao = App.favouriteSession.favouriteInforDao


    fun loadAll(): List<FavouriteInfor> {
        return favouriteInforDao.loadAll()
    }

    fun insert(favouriteInfor: FavouriteInfor?) {
        favouriteInforDao.insert(favouriteInfor)
    }


    fun del(favouriteInfor: FavouriteInfor?) {
        favouriteInforDao.delete(favouriteInfor)
    }

    fun updata(favouriteInfor: FavouriteInfor?) {
        favouriteInforDao.update(favouriteInfor)
    }
}
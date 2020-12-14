package com.example.repository.local;

import com.example.hwq_cartoon.App;
import com.example.repository.model.FavouriteInfor;
import com.example.repository.model.FavouriteInforDao;

import java.util.List;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/31
 * Time: 14:14
 */
public class CartoonDB {
    private FavouriteInforDao favouriteInforDao;
    public CartoonDB() {
        if (favouriteInforDao == null) {
            favouriteInforDao = App.favouriteSession.getFavouriteInforDao();
        }
    }

    public List<FavouriteInfor> loadAll() {
        return favouriteInforDao.loadAll();
    }

    public void insert(FavouriteInfor favouriteInfor) {
        favouriteInforDao.insert(favouriteInfor);
    }


    public void del(FavouriteInfor favouriteInfor) {
        favouriteInforDao.delete(favouriteInfor);

    }

    public void updata(FavouriteInfor favouriteInfor) {
        favouriteInforDao.update(favouriteInfor);
    }

    public void close() {
        App.favouriteSession.getDatabase().close();
        App.favouriteSession.clear();
    }
}

package com.example.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.example.repository.model.DaoMaster;
import com.example.repository.model.DaoSession;
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
    private DaoSession daoSession;

    public CartoonDB(Context context) {
        if (favouriteInforDao == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "favourite.db");
            SQLiteDatabase database = devOpenHelper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(database);
            daoSession = daoMaster.newSession();
            favouriteInforDao = daoSession.getFavouriteInforDao();
        }
    }

    public List<FavouriteInfor> loadAll() {
        return favouriteInforDao.loadAll();
    }

    public void insert(String url, String imgUrl, String title) {
        favouriteInforDao.insert(new FavouriteInfor(url, imgUrl, title));
    }


    public void del(FavouriteInfor favouriteInfor) {
        favouriteInforDao.delete(favouriteInfor);
    }

    public void updata(FavouriteInfor favouriteInfor) {
        favouriteInforDao.update(favouriteInfor);
    }

    public void close() {
        daoSession.getDatabase().close();
        daoSession.clear();
    }
}

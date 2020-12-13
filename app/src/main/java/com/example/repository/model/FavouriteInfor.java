package com.example.repository.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/31
 * Time: 14:29
 */
@Entity
public class FavouriteInfor {
    @Id(autoincrement = true)
    private Long id;
    private int mark;//mark为上次看的集数
    private String url, imgUrl, title;

    public FavouriteInfor(String url, String imgUrl, String title) {
        this.url = url;
        this.imgUrl = imgUrl;
        this.title = title;
    }


    public FavouriteInfor(int mark,  String url, String imgUrl, String title) {
        this.mark = mark;
        this.url = url;
        this.imgUrl = imgUrl;
        this.title = title;

    }

    @Generated(hash = 1148601190)
    public FavouriteInfor() {
    }


    @Generated(hash = 1297766615)
    public FavouriteInfor(Long id, int mark, String url, String imgUrl, String title) {
        this.id = id;
        this.mark = mark;
        this.url = url;
        this.imgUrl = imgUrl;
        this.title = title;
    }


    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getMark() {
        return this.mark;
    }


    public void setMark(int mark) {
        this.mark = mark;
    }


}

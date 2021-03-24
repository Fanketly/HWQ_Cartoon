package com.example.repository.model;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/18
 * Time: 0:59
 */
public class CartoonInfor {
    private String title,href,img,type;

    public CartoonInfor(String titile, String href, String img, String type) {
        this.title = titile;
        this.href = href;
        this.img = img;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CartoonInfor(String titile, String href, String img) {
        this.title = titile;
        this.href = href;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public CartoonInfor(String titile, String href) {
        this.title = titile;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

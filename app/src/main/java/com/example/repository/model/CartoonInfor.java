package com.example.repository.model;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/18
 * Time: 0:59
 */
public class CartoonInfor {
    private String titile,href,img,type;

    public CartoonInfor(String titile, String href, String img, String type) {
        this.titile = titile;
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
        this.titile = titile;
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
        this.titile = titile;
        this.href = href;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

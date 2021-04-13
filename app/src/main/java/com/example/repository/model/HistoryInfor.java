package com.example.repository.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Android Studio.
 * @author: HuangWeiQiang
 * @date:  2020/12/13
 * @time:  22:27
 */
@Entity
public class HistoryInfor {
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String imgUrl;
    private String href;
    private int mark;
    private String time;

    public HistoryInfor(String title, String imgUrl, String href, int mark, String time) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.href = href;
        this.mark = mark;
        this.time = time;
    }

    @Keep
    public HistoryInfor(Long id, String title, String imgUrl, String href, int mark,
                        String time) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.href = href;
        this.mark = mark;
        this.time = time;
    }

    @Keep
    public HistoryInfor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getMark() {
        return this.mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

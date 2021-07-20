package com.example.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android Studio.
 *
 * @author: HuangWeiQiang
 * @date: 2021/7/19
 * @time: 15:43
 */
public class KBNewestInfo {

    /**
     * code : 200
     * message : 请求成功
     * results : {"list":[{"name":"第20话","datetime_created":"2021-07-19","comic":{"name":"重生歸來的戰士","path_word":"csgldzs","females":[],"males":[],"author":[{"name":"NongNong","path_word":"nongnong"},{"name":"Sadoyeon","path_word":"sadoyeon"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/csgldzs/cover/5d963c89-e85d-11eb-90e8-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"第20话"}},{"name":"第20话","datetime_created":"2021-07-19","comic":{"name":"廢柴重生之我要當大佬","path_word":"fccszwyddl","females":[],"males":[],"author":[{"name":"JUN","path_word":"jun2"},{"name":"tomassi","path_word":"tomassi"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/fccszwyddl/cover/4607de55-e845-11eb-90d8-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"第20话"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"やすらぎのデューオ","path_word":"yasuraginoduo","females":[],"males":[],"author":[{"name":"あいなめ","path_word":"Ainame"},{"name":"はなぶささとし","path_word":"HanabusaSatoshi"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/yasuraginoduo/cover/d9b24759-e83a-11eb-90d5-024352452ce0.jpg!kb_m_item","popular":97,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"短篇","datetime_created":"2021-07-19","comic":{"name":"Look back","path_word":"lookback","females":[],"males":[],"author":[{"name":"藤本タツキ","path_word":"tengbentaciki"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/lookback/cover/fb28aad5-e836-11eb-90d3-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"短篇"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"侑ちゃんの頭にイヤホンを","path_word":"yuuchannoatamaniearphoneo","females":[],"males":[],"author":[{"name":"犬飼りっぽ","path_word":"quansirippo"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/yuuchannoatamaniearphoneo/cover/975bc131-e82e-11eb-90c3-024352452ce0.jpg!kb_m_item","popular":111,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"身長は同じくらいなのに身体の部位の大きさが全然違う二人の話","path_word":"shinchouwaonajikurainanonikaradanobuiookisagazenzenchigaufutarinohanashi","females":[],"males":[],"author":[{"name":"アウェイ田","path_word":"aweida"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/shinchouwaonajikurainanonikaradanobuiookisagazenzenchigaufutarinohanashi/cover/16586335-e82e-11eb-90c2-024352452ce0.jpg!kb_m_item","popular":111,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"不懂浪漫奇幻小說就死定了","path_word":"budonglangmanqihuanxiaojiusidingliao","females":[],"males":[],"author":[{"name":"문시현","path_word":"munsihyeon"},{"name":"사담","path_word":"sadam"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/budonglangmanqihuanxiaojiusidingliao/cover/f9f1fdf2-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":915,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"姐妹盡在不言中","path_word":"jiemeijinzaibuyanzhong","females":[],"males":[],"author":[{"name":"野澤佑季惠","path_word":"yezeyoujihui"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/jiemeijinzaibuyanzhong/cover/d6e6a718-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":203,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"阿菊小姐想要搞姬附身","path_word":"ajuxiaojiexiangyaogaojifushen","females":[],"males":[],"author":[{"name":"结野ちり","path_word":"jieyechili"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/ajuxiaojiexiangyaogaojifushen/cover/9d5b085e-e7df-11eb-a5bb-00163e0ca5bd.jpg!kb_m_item","popular":612,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"第00话","datetime_created":"2021-07-18","comic":{"name":"迷你四驅王\u2014MINI4KING","path_word":"minisiquwang","females":[],"males":[],"author":[{"name":"今田ユウキ","path_word":"imadayuki"},{"name":"武井宏之","path_word":"wjhz"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/minisiquwang/cover/75e15a58-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":153,"datetime_updated":"2021-07-18","last_chapter_name":"第00话"}},{"name":"全一話","datetime_created":"2021-07-18","comic":{"name":"恥っっっっっっず!!!","path_word":"hazzzzzzzu","females":[],"males":[],"author":[{"name":"みやまき","path_word":"miyamaki"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/hazzzzzzzu/cover/cd86ce5b-e7ad-11eb-9072-024352452ce0.jpg!kb_m_item","popular":588,"datetime_updated":"2021-07-18","last_chapter_name":"全一話"}},{"name":"全一話","datetime_created":"2021-07-18","comic":{"name":"友達とスカート丈膝上10センチに挑戦した時の話","path_word":"tomodachitoskirttakehizaue10cmnichousenshitatokinohanashi","females":[],"males":[],"author":[{"name":"アウェイ田","path_word":"aweida"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/tomodachitoskirttakehizaue10cmnichousenshitatokinohanashi/cover/5679eb5f-e7ac-11eb-9072-024352452ce0.jpg!kb_m_item","popular":1492,"datetime_updated":"2021-07-18","last_chapter_name":"全一話"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"【靈異】特殊靈能調查班","path_word":"lingyiteshulingnengtiaochaban","females":[],"males":[],"author":[{"name":"득주","path_word":"deakju"},{"name":"수사반","path_word":"dsadw"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/lingyiteshulingnengtiaochaban/cover/64c39fc6-e783-11eb-91d0-00163e0ca5bd.jpg!kb_m_item","popular":139,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"第01期","datetime_created":"2021-07-18","comic":{"name":"布偶浪人貓","path_word":"buoulangrenmao","females":[],"males":[],"author":[{"name":"凯文·伊斯特曼工作室","path_word":"kevineastmangongzuoshi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/buoulangrenmao/cover/d65661a6-e782-11eb-8540-00163e0ca5bd.jpg!kb_m_item","popular":156,"datetime_updated":"2021-07-18","last_chapter_name":"第01期"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"紡織花、庇護之神","path_word":"fangzhihuabihuzhishen","females":[],"males":[],"author":[{"name":"丹野いち子","path_word":"danyeiqiko"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/fangzhihuabihuzhishen/cover/7900d2c0-e782-11eb-bf4a-00163e0ca5bd.jpg!kb_m_item","popular":287,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"貓咪不懂報恩","path_word":"maomibudongbaoen","females":[],"males":[],"author":[{"name":"一井かずみ","path_word":"yijinggaaxi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/maomibudongbaoen/cover/3a178234-e782-11eb-bf4a-00163e0ca5bd.jpg!kb_m_item","popular":200,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"全力媚藥移動","path_word":"quanlimeiyaoyidong","females":[],"males":[],"author":[{"name":"狼太郎","path_word":"langtailang"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/quanlimeiyaoyidong/cover/181e4172-e782-11eb-8540-00163e0ca5bd.jpg!kb_m_item","popular":300,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"不行！步夢","path_word":"buxingbumeng","females":[],"males":[],"author":[{"name":"狼太郎","path_word":"langtailang"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/buxingbumeng/cover/f90f8264-e781-11eb-b484-00163e0ca5bd.jpg!kb_m_item","popular":185,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"距離初戀、徒步1分鐘","path_word":"julichuliantubuyifenzhong","females":[],"males":[],"author":[{"name":"原田唯衣","path_word":"yuantianweiyi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/julichuliantubuyifenzhong/cover/c3d61216-e781-11eb-91d0-00163e0ca5bd.jpg!kb_m_item","popular":545,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"Re:Modeling改造人之戰","path_word":"remodelinggaizaorenzhizhan","females":[],"males":[],"author":[{"name":"大野将磨","path_word":"dayejiangmo"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/remodelinggaizaorenzhizhan/cover/f1c611c8-e77a-11eb-91a1-00163e0ca5bd.jpg!kb_m_item","popular":161,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}}],"total":313,"limit":20,"offset":0}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    /**
     * list : [{"name":"第20话","datetime_created":"2021-07-19","comic":{"name":"重生歸來的戰士","path_word":"csgldzs","females":[],"males":[],"author":[{"name":"NongNong","path_word":"nongnong"},{"name":"Sadoyeon","path_word":"sadoyeon"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/csgldzs/cover/5d963c89-e85d-11eb-90e8-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"第20话"}},{"name":"第20话","datetime_created":"2021-07-19","comic":{"name":"廢柴重生之我要當大佬","path_word":"fccszwyddl","females":[],"males":[],"author":[{"name":"JUN","path_word":"jun2"},{"name":"tomassi","path_word":"tomassi"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/fccszwyddl/cover/4607de55-e845-11eb-90d8-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"第20话"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"やすらぎのデューオ","path_word":"yasuraginoduo","females":[],"males":[],"author":[{"name":"あいなめ","path_word":"Ainame"},{"name":"はなぶささとし","path_word":"HanabusaSatoshi"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/yasuraginoduo/cover/d9b24759-e83a-11eb-90d5-024352452ce0.jpg!kb_m_item","popular":97,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"短篇","datetime_created":"2021-07-19","comic":{"name":"Look back","path_word":"lookback","females":[],"males":[],"author":[{"name":"藤本タツキ","path_word":"tengbentaciki"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/lookback/cover/fb28aad5-e836-11eb-90d3-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"短篇"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"侑ちゃんの頭にイヤホンを","path_word":"yuuchannoatamaniearphoneo","females":[],"males":[],"author":[{"name":"犬飼りっぽ","path_word":"quansirippo"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/yuuchannoatamaniearphoneo/cover/975bc131-e82e-11eb-90c3-024352452ce0.jpg!kb_m_item","popular":111,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"全一話","datetime_created":"2021-07-19","comic":{"name":"身長は同じくらいなのに身体の部位の大きさが全然違う二人の話","path_word":"shinchouwaonajikurainanonikaradanobuiookisagazenzenchigaufutarinohanashi","females":[],"males":[],"author":[{"name":"アウェイ田","path_word":"aweida"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/shinchouwaonajikurainanonikaradanobuiookisagazenzenchigaufutarinohanashi/cover/16586335-e82e-11eb-90c2-024352452ce0.jpg!kb_m_item","popular":111,"datetime_updated":"2021-07-19","last_chapter_name":"全一話"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"不懂浪漫奇幻小說就死定了","path_word":"budonglangmanqihuanxiaojiusidingliao","females":[],"males":[],"author":[{"name":"문시현","path_word":"munsihyeon"},{"name":"사담","path_word":"sadam"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/budonglangmanqihuanxiaojiusidingliao/cover/f9f1fdf2-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":915,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"姐妹盡在不言中","path_word":"jiemeijinzaibuyanzhong","females":[],"males":[],"author":[{"name":"野澤佑季惠","path_word":"yezeyoujihui"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/jiemeijinzaibuyanzhong/cover/d6e6a718-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":203,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"阿菊小姐想要搞姬附身","path_word":"ajuxiaojiexiangyaogaojifushen","females":[],"males":[],"author":[{"name":"结野ちり","path_word":"jieyechili"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/ajuxiaojiexiangyaogaojifushen/cover/9d5b085e-e7df-11eb-a5bb-00163e0ca5bd.jpg!kb_m_item","popular":612,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"第00话","datetime_created":"2021-07-18","comic":{"name":"迷你四驅王\u2014MINI4KING","path_word":"minisiquwang","females":[],"males":[],"author":[{"name":"今田ユウキ","path_word":"imadayuki"},{"name":"武井宏之","path_word":"wjhz"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/minisiquwang/cover/75e15a58-e7df-11eb-bd59-00163e0ca5bd.jpg!kb_m_item","popular":153,"datetime_updated":"2021-07-18","last_chapter_name":"第00话"}},{"name":"全一話","datetime_created":"2021-07-18","comic":{"name":"恥っっっっっっず!!!","path_word":"hazzzzzzzu","females":[],"males":[],"author":[{"name":"みやまき","path_word":"miyamaki"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/hazzzzzzzu/cover/cd86ce5b-e7ad-11eb-9072-024352452ce0.jpg!kb_m_item","popular":588,"datetime_updated":"2021-07-18","last_chapter_name":"全一話"}},{"name":"全一話","datetime_created":"2021-07-18","comic":{"name":"友達とスカート丈膝上10センチに挑戦した時の話","path_word":"tomodachitoskirttakehizaue10cmnichousenshitatokinohanashi","females":[],"males":[],"author":[{"name":"アウェイ田","path_word":"aweida"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/tomodachitoskirttakehizaue10cmnichousenshitatokinohanashi/cover/5679eb5f-e7ac-11eb-9072-024352452ce0.jpg!kb_m_item","popular":1492,"datetime_updated":"2021-07-18","last_chapter_name":"全一話"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"【靈異】特殊靈能調查班","path_word":"lingyiteshulingnengtiaochaban","females":[],"males":[],"author":[{"name":"득주","path_word":"deakju"},{"name":"수사반","path_word":"dsadw"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/lingyiteshulingnengtiaochaban/cover/64c39fc6-e783-11eb-91d0-00163e0ca5bd.jpg!kb_m_item","popular":139,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"第01期","datetime_created":"2021-07-18","comic":{"name":"布偶浪人貓","path_word":"buoulangrenmao","females":[],"males":[],"author":[{"name":"凯文·伊斯特曼工作室","path_word":"kevineastmangongzuoshi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/buoulangrenmao/cover/d65661a6-e782-11eb-8540-00163e0ca5bd.jpg!kb_m_item","popular":156,"datetime_updated":"2021-07-18","last_chapter_name":"第01期"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"紡織花、庇護之神","path_word":"fangzhihuabihuzhishen","females":[],"males":[],"author":[{"name":"丹野いち子","path_word":"danyeiqiko"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/fangzhihuabihuzhishen/cover/7900d2c0-e782-11eb-bf4a-00163e0ca5bd.jpg!kb_m_item","popular":287,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"貓咪不懂報恩","path_word":"maomibudongbaoen","females":[],"males":[],"author":[{"name":"一井かずみ","path_word":"yijinggaaxi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/maomibudongbaoen/cover/3a178234-e782-11eb-bf4a-00163e0ca5bd.jpg!kb_m_item","popular":200,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"全力媚藥移動","path_word":"quanlimeiyaoyidong","females":[],"males":[],"author":[{"name":"狼太郎","path_word":"langtailang"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/quanlimeiyaoyidong/cover/181e4172-e782-11eb-8540-00163e0ca5bd.jpg!kb_m_item","popular":300,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"不行！步夢","path_word":"buxingbumeng","females":[],"males":[],"author":[{"name":"狼太郎","path_word":"langtailang"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/buxingbumeng/cover/f90f8264-e781-11eb-b484-00163e0ca5bd.jpg!kb_m_item","popular":185,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"短篇","datetime_created":"2021-07-18","comic":{"name":"距離初戀、徒步1分鐘","path_word":"julichuliantubuyifenzhong","females":[],"males":[],"author":[{"name":"原田唯衣","path_word":"yuantianweiyi"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/julichuliantubuyifenzhong/cover/c3d61216-e781-11eb-91d0-00163e0ca5bd.jpg!kb_m_item","popular":545,"datetime_updated":"2021-07-18","last_chapter_name":"短篇"}},{"name":"第01话","datetime_created":"2021-07-18","comic":{"name":"Re:Modeling改造人之戰","path_word":"remodelinggaizaorenzhizhan","females":[],"males":[],"author":[{"name":"大野将磨","path_word":"dayejiangmo"}],"img_type":2,"theme":[],"cover":"https://mirror2.mangafunc.fun/comic/remodelinggaizaorenzhizhan/cover/f1c611c8-e77a-11eb-91a1-00163e0ca5bd.jpg!kb_m_item","popular":161,"datetime_updated":"2021-07-18","last_chapter_name":"第01话"}}]
     * total : 313
     * limit : 20
     * offset : 0
     */

    @SerializedName("results")
    private ResultsDTO results;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultsDTO getResults() {
        return results;
    }

    public void setResults(ResultsDTO results) {
        this.results = results;
    }

    public static class ResultsDTO {
        @SerializedName("total")
        private int total;
        @SerializedName("limit")
        private int limit;
        @SerializedName("offset")
        private int offset;
        /**
         * name : 第20话
         * datetime_created : 2021-07-19
         * comic : {"name":"重生歸來的戰士","path_word":"csgldzs","females":[],"males":[],"author":[{"name":"NongNong","path_word":"nongnong"},{"name":"Sadoyeon","path_word":"sadoyeon"}],"img_type":1,"theme":[],"cover":"https://mirror.mangafunc.fun/comic/csgldzs/cover/5d963c89-e85d-11eb-90e8-024352452ce0.jpg!kb_m_item","popular":0,"datetime_updated":"2021-07-19","last_chapter_name":"第20话"}
         */

        @SerializedName("list")
        private List<ListDTO> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public static class ListDTO {
            @SerializedName("name")
            private String name;
            @SerializedName("datetime_created")
            private String datetimeCreated;
            /**
             * name : 重生歸來的戰士
             * path_word : csgldzs
             * females : []
             * males : []
             * author : [{"name":"NongNong","path_word":"nongnong"},{"name":"Sadoyeon","path_word":"sadoyeon"}]
             * img_type : 1
             * theme : []
             * cover : https://mirror.mangafunc.fun/comic/csgldzs/cover/5d963c89-e85d-11eb-90e8-024352452ce0.jpg!kb_m_item
             * popular : 0
             * datetime_updated : 2021-07-19
             * last_chapter_name : 第20话
             */

            @SerializedName("comic")
            private ComicDTO comic;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDatetimeCreated() {
                return datetimeCreated;
            }

            public void setDatetimeCreated(String datetimeCreated) {
                this.datetimeCreated = datetimeCreated;
            }

            public ComicDTO getComic() {
                return comic;
            }

            public void setComic(ComicDTO comic) {
                this.comic = comic;
            }

            public static class ComicDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("path_word")
                private String pathWord;
                @SerializedName("img_type")
                private int imgType;
                @SerializedName("cover")
                private String cover;
                @SerializedName("popular")
                private int popular;
                @SerializedName("datetime_updated")
                private String datetimeUpdated;
                @SerializedName("last_chapter_name")
                private String lastChapterName;
                @SerializedName("females")
                private List<?> females;
                @SerializedName("males")
                private List<?> males;
                /**
                 * name : NongNong
                 * path_word : nongnong
                 */

                @SerializedName("author")
                private List<AuthorDTO> author;
                @SerializedName("theme")
                private List<?> theme;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPathWord() {
                    return pathWord;
                }

                public void setPathWord(String pathWord) {
                    this.pathWord = pathWord;
                }

                public int getImgType() {
                    return imgType;
                }

                public void setImgType(int imgType) {
                    this.imgType = imgType;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getPopular() {
                    return popular;
                }

                public void setPopular(int popular) {
                    this.popular = popular;
                }

                public String getDatetimeUpdated() {
                    return datetimeUpdated;
                }

                public void setDatetimeUpdated(String datetimeUpdated) {
                    this.datetimeUpdated = datetimeUpdated;
                }

                public String getLastChapterName() {
                    return lastChapterName;
                }

                public void setLastChapterName(String lastChapterName) {
                    this.lastChapterName = lastChapterName;
                }

                public List<?> getFemales() {
                    return females;
                }

                public void setFemales(List<?> females) {
                    this.females = females;
                }

                public List<?> getMales() {
                    return males;
                }

                public void setMales(List<?> males) {
                    this.males = males;
                }

                public List<AuthorDTO> getAuthor() {
                    return author;
                }

                public void setAuthor(List<AuthorDTO> author) {
                    this.author = author;
                }

                public List<?> getTheme() {
                    return theme;
                }

                public void setTheme(List<?> theme) {
                    this.theme = theme;
                }

                public static class AuthorDTO {
                    @SerializedName("name")
                    private String name;
                    @SerializedName("path_word")
                    private String pathWord;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getPathWord() {
                        return pathWord;
                    }

                    public void setPathWord(String pathWord) {
                        this.pathWord = pathWord;
                    }
                }
            }
        }
    }
}

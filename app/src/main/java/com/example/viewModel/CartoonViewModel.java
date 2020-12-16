package com.example.viewModel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.repository.local.CartoonDB;
import com.example.repository.model.CartoonInfor;
import com.example.repository.model.FavouriteInfor;
import com.example.util.NetworkUtils;
import com.example.util.YhshThreadPoolFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/18
 * Time: 18:13
 */
public class CartoonViewModel extends AndroidViewModel {
    private final String TAG = "TAG";

    public CartoonViewModel(@NonNull Application application) {
        super(application);
        cartoonModel = new CartoonDB();
        Log.i(TAG, "CREATE: ");
    }
//监听是否隐藏bottom
    private final MutableLiveData<Boolean>bottomLiveData=new MutableLiveData<>();
    public MutableLiveData<Boolean> getBottomLiveData() {
        return bottomLiveData;
    }
    //msg2主页漫画
    private List<CartoonInfor> cartoonInfors = new ArrayList<>();
    private MutableLiveData<List<CartoonInfor>> liveDataCartoon = new MutableLiveData<>();
    private static final String Url3 = "https://manhua.dmzj.com/";
    private int pager = 1;
    public MutableLiveData<List<CartoonInfor>> getLiveDataCartoon() {
        return liveDataCartoon;
    }

    //msg3集数
    private List<CartoonInfor> mgs3List = new ArrayList<>();
    private MutableLiveData<List<CartoonInfor>> liveDataMsg3 = new MutableLiveData<>();
    private String content;
    public List<CartoonInfor> getMgs3List() {
        return mgs3List;
    }

    public MutableLiveData<List<CartoonInfor>> getLiveDataMsg3() {
        return liveDataMsg3;
    }

    public String getContent() {
        return content;
    }

    //msg4显示漫画
    private List<String> msg4List = new ArrayList<>();
    private List<byte[]> imgList = new ArrayList<>();
    private MutableLiveData<List<byte[]>> liveDataMsg4 = new MutableLiveData<>();

    public MutableLiveData<List<byte[]>> getLiveDataMsg4() {
        return liveDataMsg4;
    }

    public List<String> getMsg4List() {
        return msg4List;
    }

    //search
    private List<CartoonInfor> listMsg5 = new ArrayList<>();
    private MutableLiveData<List<CartoonInfor>> cartoonInforsSearch = new MutableLiveData<>();

//    public List<CartoonInfor> getListMsg5() {
//        return listMsg5;
//    }
//
//    public MutableLiveData<List<CartoonInfor>> getCartoonInforsSearch() {
//        return cartoonInforsSearch;
//    }

    //favourite
    private final CartoonDB cartoonModel;
    //top
    private List<CartoonInfor> cartoonHome = new ArrayList<>();
    private final Handler handler = new Handler(msg -> {
        if (msg.what == 1) {//图片
            Document document = Jsoup.parse((String) msg.obj);
            Elements elements = document.getElementsByTag("script");
            if (elements.size() != 0) {
                String[] elScriptList = elements.get(0).data().split(";");
                String s6 = elScriptList[6];//7
                String s7 = elScriptList[7];//第8个u
                String[] strings = s6.split(",");//地址分割
                String urlheard = null;
                String ss = s7.substring(0, s7.indexOf(".") - 1);
                Collections.addAll(msg4List, ss.split("\\|"));
                String s0 = msg4List.get(0);
                msg4List.set(0, s0.substring(s0.lastIndexOf("'") + 1));//分割后得到的 3:AD 4:E7 5:E4 6:90 7:A9 8:E6
                int n = 0;
                int mark = 0;
                for (String a : msg4List
                ) {
                    Log.i(TAG, n + ": " + a);
                    n++;
                }
                for (int i = 2, length = strings.length; i < length; i++) {//地址分割
                    String s = strings[i];
                    if (i == 2) {//储存第一个地址的前面固定形式
                        Log.i(TAG, "s2: " + s);//获取到的第一个地址
                        s = s.substring(s.lastIndexOf("[") + 2, s.lastIndexOf("\""));
                        String[] sss = s.split("\\\\\\\\");
                        Log.i(TAG, "onCreate: " + Arrays.toString(sss));//处理之后的第一个
                        StringBuilder stringBuffer = new StringBuilder();
                        char c = sss[0].charAt(0);
                        String sss0;
                        if (conversion(c) >= msg4List.size())
                            sss0 = String.valueOf(c);
                        else
                            sss0 = getStringList2(conversion(sss[0].charAt(0)));
                        String url = "https://images.dmzj.com/";
                        if (sss0.equals(""))//判断*/y/*的y
                            stringBuffer.append(url).append(sss[0]).append("/");
                        else
                            stringBuffer.append(url).append(sss0).append("/");
                        String[] ssss;
                        for (int j = 1, sss_length = sss.length; j < sss_length; j++) {
                            String s3 = sss[j];
                            if (j == sss_length - 1) {//末尾页数
                                urlheard = stringBuffer.toString();
                                Log.i(TAG, "s3" + s3);
                                if (s3.contains("%")) {
                                    ssss = s3.split("%");
                                    String ss0 = ssss[0].replace("/", "");
                                    if (!ss0.equals("")) {
                                        if (ss0.length() == 1)
                                            stringBuffer.append(getStringList(conversion(ss0.charAt(0))));
                                        else
                                            stringBuffer.append(getStringList(conversionString(ss0)));
                                    }
                                    String sk;
                                    for (int k = 1, ssss_length = ssss.length; k < ssss_length; k++) {
                                        sk = ssss[k];
                                        stringBuffer.append("%");
                                        if (k == ssss_length - 1) {
                                            sk = (sk.substring(0, sk.indexOf(".")));
                                            stringBuffer.append(getStringList((conversion(sk.charAt(0))))).append(".").append(getStringList(conversion(s3.charAt(s3.length() - 1))));
                                        } else {
                                            stringBuffer.append(getStringList((conversion(sk.charAt(0)))));
                                        }
                                    }
                                } else if (s3.contains("-")) {
                                    stringBuffer.append(getStringList(conversion(s3.charAt(1))))
                                            .append("-").append(getStringList(conversion(s3.charAt(3))))
                                            .append(".").append(getStringList(conversion(s3.charAt(5))));
                                } else if (s3.contains("/")) {
                                    stringBuffer.append(getStringList(conversion(s3.charAt(1))))
                                            .append(".")
                                            .append(getStringList(conversion(s3.charAt(3))));
                                }
                            } else {
                                Log.i(TAG, "ss3: " + s3);
                                ssss = s3.split("%");
                                String ssss0 = ssss[0];
                                if (!ssss0.equals("") && !ssss0.equals("/")) {//判断%前面是否有
                                    ssss0 = ssss0.replace("/", "");
                                    if (ssss0.contains(".")) {
                                        stringBuffer.append(ssss0);
                                    } else {
                                        stringBuffer.append(getStringList(conversion(ssss0.charAt(0))));
                                    }
                                    Log.i(TAG, "p: " + ssss0);
                                }
                                String ssssk;
                                for (int k = 1, ssss_length = ssss.length; k < ssss_length; k++) {
                                    ssssk = ssss[k];
                                    if (ssssk.length() == 1) {
                                        stringBuffer.append("%").append(getStringList(conversion(ssss[k].charAt(0))));
                                    } else if (ssssk.contains(".")) {
                                        stringBuffer.append("%").append(getStringList(conversion(ssss[k].charAt(0))))
                                                .append(".")
                                                .append(getStringList(conversion(ssss[k].charAt(2))));
                                    } else if (ssssk.contains("-")) {
                                        stringBuffer.append("%").append(getStringList(conversion(ssss[k].charAt(0))))
                                                .append("-")
                                                .append(getStringList(conversion(ssss[k].charAt(2))));
                                    }
                                }
                                stringBuffer.append("/");
                            }
                        }
                        send(stringBuffer.toString());
                    } else {
                        String a = s.substring(s.lastIndexOf("/") + 1, s.length() - 1);
                        if (i == strings.length - 1) {
                            a = a.substring(0, a.length() - 3);
                        }
                        mark++;
                        Log.i(TAG, mark + ": " + a);//末尾页数
                        if (a.length() == 3 && urlheard != null) {//末尾是否长度为3
                            send(urlheard +
                                    getStringList(conversion(a.charAt(0))) +
                                    "." +
                                    getStringList(conversion(a.charAt(2))));
                        } else {
                            StringBuilder stringBuffer = new StringBuilder();
                            String[] b = a.split("%");
                            String bj;
                            for (int j = 0, b_length = b.length; j < b_length; j++) {
                                bj = b[j];
                                if (!bj.equals("")) {
                                    if (j == b_length - 1) {//判断是否包含%
                                        bj = bj.substring(0, bj.lastIndexOf("."));
                                    }
                                    if (bj.length() >= 2) {
                                        if (bj.contains("-")) {
                                            Split(bj, stringBuffer, "-");
                                        } else if (bj.contains(".")) {
                                            Split(bj, stringBuffer, "\\.");
                                        } else if (bj.contains("~")) {
                                            Split(bj, stringBuffer, "~");
                                        } else {
                                            twoWords(bj, stringBuffer);
                                        }

                                    } else {
                                        stringBuffer.append(getStringList(conversion(bj.charAt(0))));
                                    }
                                }
                                if (j != b.length - 1) stringBuffer.append("%");
                            }
                            send(urlheard +
                                    stringBuffer +
                                    "." +
                                    getStringList(conversion(a.charAt(a.length() - 1))));

                        }
                    }
                }
            } else {
                Log.i(TAG, "所选漫画消失");
            }
        } else if (msg.what == 2) {//漫画
            Document document = Jsoup.parse((String) msg.obj);
            Elements element = document.getElementsByClass("newpic_content");
            Elements elements = element.get(0).getElementsByClass("boxdiv1");
            Element element1;
            Element element2;
            Element element3;
            CartoonInfor cartoonInfor;
            for (Element e : elements
            ) {
                element1 = e.select(".picborder a").first();//图片
                element2 = e.select(".picborder img").first();
                element3 = e.select(".pictext li").get(2);
                cartoonInfor = new CartoonInfor(element1.attr("title"), element1.attr("href"), element2.attr("src"), element3.text());
                cartoonInfors.add(cartoonInfor);
            }
            liveDataCartoon.setValue(cartoonInfors);
        } else if (msg.what == 3) {//集数
            Document document = Jsoup.parse((String) msg.obj);
            Elements elements = document.getElementsByClass("cartoon_online_border");
            content = document.select(".line_height_content").text();
            Elements elements1 = elements.select("a");
            CartoonInfor cartoonInfor;
            for (Element e : elements1) {
                cartoonInfor = new CartoonInfor(e.text(), e.attr("href"));
                mgs3List.add(cartoonInfor);
            }
            if (mgs3List.size() > 0) liveDataMsg3.setValue(mgs3List);

        } else if (msg.what == 4) {//显示漫画
            imgList.add((byte[]) msg.obj);
            liveDataMsg4.setValue(imgList);
        } else if (msg.what == 5) {//查询
            String s = (String) msg.obj;
            if (s.length() > 0) {
                String[] ss = s.split("\\{");
                CartoonInfor cartoonInfor;
                if (ss.length > 10) {
                    String[] ss2;
                    String s51;
                    String s56;
                    String s54;
                    for (int i = 1; i <= 10; i++) {
                        ss2 = ss[i].split(",");
                        //4 图 6 地址 1 名称
                        s51 = ss2[1];
                        s56 = ss2[6];
                        s54 = ss2[4];
                        cartoonInfor = new CartoonInfor(decode(s51.substring(14, s51.length() - 1)), "https://" + s56.substring(21, s56.length() - 1)
                                .replaceAll("\\\\", ""), s54.substring(9, s54.length() - 1).replaceAll("\\\\", ""));
                        listMsg5.add(cartoonInfor);
                    }
                } else {
                    String[] ss2;
                    String s51;
                    String s56;
                    String s54;
                    for (int i = 1; i < ss.length; i++) {
                        ss2 = ss[i].split(",");
                        //4 图 6 地址 1 名称
                        s51 = ss2[1];
                        s56 = ss2[6];
                        s54 = ss2[4];
                        cartoonInfor = new CartoonInfor(decode(s51.substring(14, s51.length() - 1)), "https://" + s56.substring(21, s56.length() - 1)
                                .replaceAll("\\\\", ""), s54.substring(9, s54.length() - 1).replaceAll("\\\\", ""));
                        listMsg5.add(cartoonInfor);
                    }
                }
                cartoonInforsSearch.setValue(listMsg5);
            }
        } else if (msg.what == 6) {//home
            Document document = Jsoup.parse((String) msg.obj);
            Elements element = document.getElementsByClass("middleright-right");
            Elements elements = element.get(0).getElementsByClass("middlerighter1");
            Element element1;
            Element element2;
            Element element3;
            CartoonInfor cartoonInfor;
            for (Element e : elements
            ) {
                element1 = e.select(".righter-img a").first();//图片
                element2 = e.select(".righter-img img").first();
                element3 = e.select(".righter-mr span").get(5);
                cartoonInfor = new CartoonInfor(element1.attr("title"), element1.attr("href"), element2.attr("src"), "分类：" + element3.text());
                cartoonHome.add(cartoonInfor);
            }
            liveDataCartoon.setValue(cartoonHome);
        }
        return false;
    });


//    /**
//     * 漫画本月人气排行
//     **/
//    public void getTopCartoon() {
//        if (cartoonHome.size() > 0) cartoonHome.clear();
//        NetworkUtils.getInstance().OkhttpGet(handler, "https://manhua.dmzj.com/rank/month-block-1.shtml", 6);
//    }
//
//
//    public void getTopCartoon(int position) {
//        String s = cartoonHome.get(position).getHref();
//        Log.i(TAG, "onClick: " + s);
//        if (s.contains("dmzj"))
//            NetworkUtils.getInstance().OkhttpGet(handler, s, 3);
//        else
//            NetworkUtils.getInstance().OkhttpGet(handler, Url3 + s, 3);
//    }
//
//    public FavouriteInfor setFavouriteTop(int position) {
//        CartoonInfor cartoonInfor = cartoonHome.get(position);
//        String s = cartoonInfor.getHref();
//        FavouriteInfor favouriteInfor;
//        if (s.contains("dmzj"))
//            favouriteInfor = new FavouriteInfor(s, cartoonInfor.getImg(), cartoonInfor.getTitile());
//        else
//            favouriteInfor = new FavouriteInfor(Url3 + s, cartoonInfor.getImg(), cartoonInfor.getTitile());
//        cartoonModel.insert(favouriteInfor);
//        return favouriteInfor;
//    }

    /**
     * 加载图片部分
     **/
    public void msg3Send(int position) {
        String url2 = "https://manhua.dmzj.com";
        NetworkUtils.getInstance().OkhttpGet(handler, url2 + mgs3List.get(position).getHref(), 1);
//                        NetworkUtils.getInstance().OkhttpGet(handler, "", 1);//测试网页用
    }

    public void onMsg3Dismiss() {//阻止继续加载图片
        mgs3List.clear();
        Log.i("TAG", "onDismiss3: ");
    }

    public void onMsg4Dismiss() {
        if (!YhshThreadPoolFactory.IsShutDown()) {
            handler.removeCallbacksAndMessages(null);
            YhshThreadPoolFactory.getInstance().shutDown();
            imgList.clear();
        }
        Log.i("TAG", "onDismiss4: ");
    }


    private void send(String url) {
        YhshThreadPoolFactory.getInstance().executeRequest(() -> {
            try {
                NetworkUtils.getInstance().send(url, handler, 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 主页
     *homeFragment
     */

    public List<CartoonInfor> getCartoonInfors() {
        return cartoonInfors;
    }

    public void getHomeCartoon(int position) {
        String s = cartoonInfors.get(position).getHref();
        Log.i(TAG, "onClick: " + s);
        if (s.equals(""))
            return;
        if (s.contains("dmzj"))
            NetworkUtils.getInstance().OkhttpGet(handler, s, 3);
        else
            NetworkUtils.getInstance().OkhttpGet(handler, Url3 + s, 3);
    }

    public FavouriteInfor setFavouriteHome(int position) {
        CartoonInfor cartoonInfor = cartoonInfors.get(position);
        String s = cartoonInfor.getHref();
        FavouriteInfor favouriteInfor;
        if (s.contains("dmzj")) {
            favouriteInfor = new FavouriteInfor(s, cartoonInfor.getImg(), cartoonInfor.getTitile());
        } else {
            favouriteInfor = new FavouriteInfor(Url3 + s, cartoonInfor.getImg(), cartoonInfor.getTitile());
        }
        cartoonModel.insert(favouriteInfor);
        return favouriteInfor;
    }

    public void nextPager() {//下一页
        pager++;
        NetworkUtils.getInstance().OkhttpGet(handler, "https://manhua.dmzj.com/update_" + pager + ".shtml", 2);
    }

    public void refreshPager() {//刷新
        pager = 1;
        cartoonInfors.clear();
        NetworkUtils.getInstance().OkhttpGet(handler, "https://manhua.dmzj.com/update_" + pager + ".shtml", 2);
    }

    public void getHomeCartoon(){
        NetworkUtils.getInstance().OkhttpGet(handler, "https://manhua.dmzj.com/update_" + pager + ".shtml", 2);
    }

    /***
     * favouriteFragment
     *
     * **/
    public List<FavouriteInfor> getFavourite() {//初始化
        return cartoonModel.loadAll();
    }

    public void setFavourite(FavouriteInfor favourite) {
        cartoonModel.insert(favourite);
    }


    public void favouriteGet(String url) {
        Log.i(TAG, "favouriteGet: " + url);
        NetworkUtils.getInstance().OkhttpGet(handler, url, 3);
    }

    public void favouriteDel(FavouriteInfor favouriteInfor) {
        cartoonModel.del(favouriteInfor);
    }
  /**
   *
   *
   * */
  public void updateFavourite(FavouriteInfor favouriteInfor) {
      cartoonModel.updata(favouriteInfor);
  }

//    /**
//     *search
//     *
//     * **/
//    public void getSearch(int position) {
//        String s = listMsg5.get(position).getHref();
//        Log.i(TAG, "onClick: " + s);
//        NetworkUtils.getInstance().OkhttpGet(handler, s, 3);
//    }
//
//    public void setSearchFavourite(int position) {
//        CartoonInfor cartoonInfor = listMsg5.get(position);
//        cartoonModel.insert(new FavouriteInfor(cartoonInfor.getHref(), cartoonInfor.getImg(), cartoonInfor.getTitile()));
//    }
//
//    public void search(String name) {
//        NetworkUtils.getInstance().OkhttpGet(handler, "https://sacg.dmzj.com/comicsum/search.php?s=" + name, 5);
//    }
//
//    public void clearListMsg5() {
//        if (listMsg5 != null)
//            if (listMsg5.size() > 0)
//                listMsg5.clear();
//    }

    /**
     * 逻辑处理部分
     */

    public String decode(String unicodeStr) {//UNICODE转中文
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    private String getStringList(int num) {//有就有，没有就返回原数值
        if (msg4List.get(num).equals("")) {
//            Log.i(TAG, "getStringList: " + num);
            return String.valueOf(num);
        }
        return msg4List.get(num);
    }

    private String getStringList2(int num) {//有就有，没有就没有
        return msg4List.get(num);

    }

    private int conversion(char s) {
        int a = 0;
        if (s >= 48 && s <= 57) {
            a = s - 48;//0-9
        } else if (s >= 65 && s <= 90) {
            a = s - 29;//36-61
        } else if (s >= 97 && s <= 122) {
            a = s - 87;//10-35
        }
        return a;
    }

    private int conversionString(String s) {
        Log.i(TAG, "conversion: " + s);
        int a = 0;
        int int_s = Integer.parseInt(s);
        if (int_s >= 10) {
            a = int_s + 51;
        }
        Log.i(TAG, "conversion: " + a);
        return a;
    }

    public void twoWords(String bj, StringBuilder stringBuffer) {//EG：11 or 1a
        Matcher matcher = Pattern.compile(".*[a-zA-Z]+.*").matcher(bj);//判断有没有包含字母
        if (matcher.matches()) {
            stringBuffer.append(getStringList2(bj.charAt(1) - 25));
        } else {
            int i_bj = Integer.parseInt(bj);
            if (i_bj >= 10 && i_bj + 52 < msg4List.size()) {//66=14 66是第67个
                String s_bj = getStringList2(i_bj + 52);
                if (!s_bj.equals("")) {
                    stringBuffer.append(s_bj);
                } else {
                    stringBuffer.append(i_bj);
                }
            } else {
                stringBuffer.append(i_bj);
            }
        }
    }

    private void Split(String bj, StringBuilder stringBuffer, String mark) {
        String[] bjs = bj.split(mark);
        for (int k = 0, bjs_length = bjs.length; k < bjs_length; k++) {
            String bj2 = bjs[k];
            if (bj2.length() == 1) {
//                                                Log.i(TAG, ": " + bj2.charAt(0));
                stringBuffer.append(getStringList(conversion(bj2.charAt(0))));
            } else if (bj2.length() == 2) {
                twoWords(bj2, stringBuffer);
            } else if (bj.contains(".")) {
                String[] bjs3 = bj2.split("\\.");
                for (int i = 0, bj3_length = bjs3.length; i < bj3_length; i++) {
                    String bj3 = bjs3[i];
                    if (bj3.length() == 1)
                        stringBuffer.append(getStringList(conversion(bj3.charAt(0))));
                    if (bj3.length() == 2)
                        twoWords(bj3, stringBuffer);
                    if (i != bj3_length - 1 || i == 0)
                        stringBuffer.append(".");
                }

            }
            if (k != bjs_length - 1 || k == 0)
                stringBuffer.append(mark);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared: ");
        cartoonModel.close();
        msg4List.clear();
        cartoonInfors.clear();
        listMsg5.clear();
        mgs3List.clear();
        cartoonHome.clear();
        cartoonHome = null;
        msg4List = null;
        cartoonInfors = null;
        listMsg5 = null;
        imgList.clear();
        imgList = null;
        mgs3List = null;
        cartoonInforsSearch = null;
        liveDataMsg4 = null;
        liveDataMsg3 = null;
        liveDataCartoon = null;
    }
}

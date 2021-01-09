package com.example.viewModel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.TAG
import com.example.base.Url3
import com.example.hwq_cartoon.R
import com.example.repository.model.*
import com.example.repository.remote.CartoonRemote
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/27
 * Time: 20:35
 */

class CartoonViewModel : ViewModel() {

    init {
        Log.i(TAG, "CREATE: ")
    }

    //species
    var species = "3255"
    val speciesList: MutableList<FavouriteInfor> = ArrayList()
    val typeList: MutableList<SpeciesInfor> = ArrayList()
    val speciesLiveData = MutableLiveData<Boolean>()

    //判断是否在searchFragment
    var isSearchFragment = false

    //bundle跳转需要传递的数据
    val bundle = Bundle()

    //监听是否隐藏bottom
    val bottomLiveData = MutableLiveData<Boolean>()

    //加载监听
    val pgLiveData = MutableLiveData<Boolean>()

    //msg2主页漫画
    val cartoonInfors: MutableList<CartoonInfor> = ArrayList()
    val homeLiveData = MutableLiveData<List<CartoonInfor>>()
    private var pager = 1

    //msg3集数
    val mgs3List: MutableList<CartoonInfor> = ArrayList()
    val msg3LiveData = MutableLiveData<List<CartoonInfor>>()
    var content: String? = null
    private val url2 = "https://manhua.dmzj.com"

    //msg4显示漫画
    val msg4List: MutableList<String> = ArrayList()
    private val imgList: MutableList<ByteArray> = ArrayList()
    val msg4LiveData = MutableLiveData<List<ByteArray>>()

    //search
    val searchList: MutableList<CartoonInfor> = ArrayList()
    val searchLiveData = MutableLiveData<Boolean>()

    //banner
    val bannerList: MutableList<CartoonInfor> = ArrayList()
    val bannerLiveData = MutableLiveData<List<CartoonInfor>>()

    private fun what3(string: String) {//集数
        val document = Jsoup.parse(string)
        val elements = document.getElementsByClass("cartoon_online_border")
        content = document.select(".line_height_content").text()
        val elements1 = elements.select("a")
        var cartoonInfor: CartoonInfor
        for (e in elements1) {
            cartoonInfor = CartoonInfor(e.text(), e.attr("href"))
            mgs3List.add(cartoonInfor)
        }
        if (mgs3List.size > 0) msg3LiveData.postValue(mgs3List)
    }


    private fun what1(string: String) {//图片
        val document = Jsoup.parse(string)
        val elements = document.getElementsByTag("script")
        if (elements.size != 0) {
            val elScriptList = elements[0].data().split(";").toTypedArray()
            val s6 = elScriptList[6] //7
            val s7 = elScriptList[7] //第8个u
            val strings = s6.split(",").toTypedArray() //地址分割
            var urlheard: String? = null
            val ss = s7.substring(0, s7.indexOf(".") - 1)
            Collections.addAll(
                msg4List,
                *ss.split("|").toTypedArray()
            )
            val s0 = msg4List[0]
            msg4List[0] =
                s0.substring(s0.lastIndexOf("'") + 1) //分割后得到的 3:AD 4:E7 5:E4 6:90 7:A9 8:E6
            var mark = 0
            for ((n, a) in msg4List.withIndex()) {
                Log.i(TAG, "$n: $a")
            }
            var i = 2
            val length = strings.size
            while (i < length) {
                //地址分割
                var s = strings[i]
                if (i == 2) { //储存第一个地址的前面固定形式
                    Log.i(TAG, "s2: $s") //获取到的第一个地址
                    s = s.substring(s.lastIndexOf("[") + 2, s.lastIndexOf("\""))
                    val sss = s.split("\\\\").toTypedArray()
                    Log.i(
                        TAG,
                        "onCreate: " + sss.contentToString()
                    ) //处理之后的第一个
                    val stringBuffer = StringBuilder()
                    val c = sss[0][0]
                    var sss0: String
                    sss0 =
                        if (conversion(c) >= msg4List.size) c.toString() else getStringList2(
                            conversion(
                                sss[0][0]
                            )
                        )
                    val url = "https://images.dmzj.com/"
                    if (sss0 == "") //判断*/y/*的y
                        stringBuffer.append(url).append(sss[0])
                            .append("/") else stringBuffer.append(url).append(sss0)
                        .append("/")
                    var ssss: Array<String>
                    var j = 1
                    val sssLength = sss.size
                    while (j < sssLength) {
                        val s3 = sss[j]
                        if (j == sssLength - 1) { //末尾页数
                            urlheard = stringBuffer.toString()
                            Log.i(TAG, "s3$s3")
                            when {
                                s3.contains("%") -> {
                                    ssss = s3.split("%").toTypedArray()
                                    val ss0 = ssss[0].replace("/", "")
                                    if (ss0 != "") {
                                        if (ss0.length == 1) stringBuffer.append(
                                            getStringList(
                                                conversion(
                                                    ss0[0]
                                                )
                                            )
                                        ) else stringBuffer.append(
                                            getStringList(
                                                conversionString(
                                                    ss0
                                                )
                                            )
                                        )
                                    }
                                    var sk: String
                                    var k = 1
                                    val ssssLength = ssss.size
                                    while (k < ssssLength) {
                                        sk = ssss[k]
                                        stringBuffer.append("%")
                                        if (k == ssssLength - 1) {
                                            sk = sk.substring(0, sk.indexOf("."))
                                            stringBuffer.append(getStringList(conversion(sk[0])))
                                                .append(".").append(
                                                    getStringList(
                                                        conversion(
                                                            s3[s3.length - 1]
                                                        )
                                                    )
                                                )
                                        } else {
                                            stringBuffer.append(getStringList(conversion(sk[0])))
                                        }
                                        k++
                                    }
                                }
                                s3.contains("-") -> {
                                    stringBuffer.append(getStringList(conversion(s3[1])))
                                        .append("-")
                                        .append(getStringList(conversion(s3[3])))
                                        .append(".")
                                        .append(getStringList(conversion(s3[5])))
                                }
                                s3.contains("/") -> {
                                    stringBuffer.append(getStringList(conversion(s3[1])))
                                        .append(".")
                                        .append(getStringList(conversion(s3[3])))
                                }
                            }
                        } else {
                            Log.i(TAG, "ss3: $s3")
                            ssss = s3.split("%").toTypedArray()
                            var ssss0 = ssss[0]
                            if (ssss0 != "" && ssss0 != "/") { //判断%前面是否有
                                ssss0 = ssss0.replace("/", "")
                                if (ssss0.contains(".")) {
                                    stringBuffer.append(ssss0)
                                } else {
                                    stringBuffer.append(getStringList(conversion(ssss0[0])))
                                }
                                Log.i(TAG, "p: $ssss0")
                            }
                            var ssssk: String
                            var k = 1
                            val ssssLength = ssss.size
                            while (k < ssssLength) {
                                ssssk = ssss[k]
                                when {
                                    ssssk.length == 1 -> {
                                        stringBuffer.append("%").append(
                                            getStringList(
                                                conversion(
                                                    ssss[k][0]
                                                )
                                            )
                                        )
                                    }
                                    ssssk.contains(".") -> {
                                        stringBuffer.append("%").append(
                                            getStringList(
                                                conversion(
                                                    ssss[k][0]
                                                )
                                            )
                                        )
                                            .append(".")
                                            .append(getStringList(conversion(ssss[k][2])))
                                    }
                                    ssssk.contains("-") -> {
                                        stringBuffer.append("%").append(
                                            getStringList(
                                                conversion(
                                                    ssss[k][0]
                                                )
                                            )
                                        )
                                            .append("-")
                                            .append(getStringList(conversion(ssss[k][2])))
                                    }
                                }
                                k++
                            }
                            stringBuffer.append("/")
                        }
                        j++
                    }
                    send(stringBuffer.toString())
                } else {
                    var a = s.substring(s.lastIndexOf("/") + 1, s.length - 1)
                    if (i == strings.size - 1) {
                        a = a.substring(0, a.length - 3)
                    }
                    mark++
                    Log.i(TAG, "$mark: $a") //末尾页数
                    if (a.length == 3 && urlheard != null) { //末尾是否长度为3
                        send(
                            urlheard +
                                    getStringList(conversion(a[0])) +
                                    "." +
                                    getStringList(conversion(a[2]))
                        )
                    } else {
                        val stringBuffer = StringBuilder()
                        val b = a.split("%").toTypedArray()
                        var bj: String
                        var j = 0
                        val bLength = b.size
                        while (j < bLength) {
                            bj = b[j]
                            if (bj != "") {
                                if (j == bLength - 1) { //判断是否包含%
                                    bj = bj.substring(0, bj.lastIndexOf("."))
                                }
                                if (bj.length >= 2) {
                                    when {
                                        bj.contains("-") -> {
                                            split(bj, stringBuffer, "-")
                                        }
                                        bj.contains(".") -> {
                                            split(bj, stringBuffer, ".")
                                        }
                                        bj.contains("~") -> {
                                            split(bj, stringBuffer, "~")
                                        }
                                        else -> {
                                            twoWords(bj, stringBuffer)
                                        }
                                    }
                                } else {
                                    stringBuffer.append(getStringList(conversion(bj[0])))
                                }
                            }
                            if (j != b.size - 1) stringBuffer.append("%")
                            j++
                        }
                        send(
                            urlheard +
                                    stringBuffer +
                                    "." +
                                    getStringList(conversion(a[a.length - 1]))
                        )
                    }
                }
                i++
            }
            loadImg()
        } else {
            Log.i(TAG, "所选漫画消失")
        }
    }

    private fun what5(s: String) {//查询
        if (s.isNotEmpty()) {
            val ss = s.split("{").toTypedArray()
            var cartoonInfor: CartoonInfor
            if (ss.size > 10) {
                var ss2: Array<String>
                var s51: String
                var s56: String
                var s54: String
                for (i in 1..10) {
                    ss2 = ss[i].split(",").toTypedArray()
                    //4 图 6 地址 1 名称
                    s51 = ss2[1]
                    s56 = ss2[6]
                    s54 = ss2[4]
                    cartoonInfor = CartoonInfor(
                        decode(s51.substring(14, s51.length - 1)),
                        "https://" + s56.substring(21, s56.length - 1)
                            .replace("\\", ""),
                        s54.substring(9, s54.length - 1).replace("\\", "")
                    )
                    searchList.add(cartoonInfor)
                }
            } else {
                var ss2: Array<String>
                var s51: String
                var s56: String
                var s54: String
                for (i in 1 until ss.size) {
                    ss2 = ss[i].split(",").toTypedArray()
                    //4 图 6 地址 1 名称
                    s51 = ss2[1]
                    s56 = ss2[6]
                    s54 = ss2[4]
                    cartoonInfor = CartoonInfor(
                        decode(s51.substring(14, s51.length - 1)),
                        "https://" + s56.substring(21, s56.length - 1)
                            .replace("\\", ""),
                        s54.substring(9, s54.length - 1).replace("\\", "")
                    )
                    searchList.add(cartoonInfor)
                }
            }
            searchLiveData.postValue(true)
        } else {
            searchLiveData.postValue(false)
        }
    }

//            1 -> { //图片
//            2 -> { //漫画
//            3 -> { //集数
//            4 -> { //显示漫画
//            5 -> { //查询
//            6 -> { //home
//            7 -> {
//            8 -> {

    private fun what6(it: String) {
        val document = Jsoup.parse(it)
        val element = document.getElementsByClass("middleright-right")
        val elements = element[0].getElementsByClass("middlerighter1")
        var element1: Element
        var element2: Element
        var element3: Element
        var cartoonInfor: CartoonInfor
        for (i in 0..4) {
            val e = elements[i]
            element1 = e.select(".righter-img a").first() //图片
            element2 = e.select(".righter-img img").first()
            element3 = e.select(".righter-mr span")[5]
            cartoonInfor = CartoonInfor(
                element1.attr("title"),
                element1.attr("href"),
                element2.attr("src"),
                "分类：" + element3.text()
            )
            bannerList.add(cartoonInfor)
        }
        bannerLiveData.postValue(bannerList)
    }

    private fun what7(str2: String) {
        val str = str2.substring(str2.indexOf("["), str2.lastIndexOf("]") + 1)
        val gson = Gson()
        val speciesInfor2s: List<SpeciesInfor2Item> =
            gson.fromJson(
                str,
                SpeciesInfor2::class.java
            )
        Log.i(TAG, ": " + speciesInfor2s[0].comic_cover)
        for ((_, comic_cover, comic_url, _, _, _, _, name) in speciesInfor2s) {
            speciesList.add(FavouriteInfor(comic_url, "https:$comic_cover", name))
        }
        speciesLiveData.postValue(true)
    }

    private fun what8(s: String) {
        typeList.add(SpeciesInfor("0", "全部"))
        val document = Jsoup.parse(s)
        val typeElms = document.getElementsByClass("search_list_m_right")[4]
        for (a in typeElms.select("a")) {
            typeList.add(
                SpeciesInfor(
                    a.attr("id").substring(5),
                    a.attr("title")
                )
            )
        }
        getSpeciesData()
    }

    //remote
    val errorLiveData = MutableLiveData<String>()
    private val remote = CartoonRemote(errorLiveData)

    /**
     * 漫画本月人气排行
     */
    fun getBanner() {
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData("https://manhua.dmzj.com/rank/month-block-1.shtml")
                .collect {
                    what6(it)
                }
        }
    }


    private fun putBundle(name: String, img: String, href: String, mark: Int) {
        bundle.clear()
        bundle.putString("name", name)
        bundle.putString("img", img)
        bundle.putString("href", href)
        bundle.putInt("mark", mark)
    }


    /**
     * 加载图片部分
     */
    fun msg3Send(position: Int) {
        pgLiveData.value = false
        job = CoroutineScope(Dispatchers.IO).launch {
            remote.getData(url2 + mgs3List[position].href).collect {
                if (imgUrlList.size > 0)
                    imgUrlList.clear()
                what1(it)
            }
        }
    }

    fun onMsg3Dismiss() { //清除集数
        mgs3List.clear()
        Log.i("TAG", "onDismiss3: ")
    }

    fun onMsg4Dismiss() {
        job.cancel()
        imgList.clear()
        Log.i("TAG", "onDismiss4: ")
    }

    private lateinit var job: Job
    private val imgUrlList = mutableListOf<String>()
    private fun loadImg() {
        pgLiveData.postValue(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            for (url in imgUrlList) {
                if (!isActive) break
                remote.getImg(url).collect {
                    if (isActive)
                        if (it != null) {
                            imgList.add(it)
                            msg4LiveData.postValue(imgList)
                        }
                }
            }
        }
    }

    private fun send(url: String) {//what4
        imgUrlList.add(url)
    }

    /**
     * 分类
     * SpeciesFragment
     */
    fun getSpeciesType() {
        species = "0"
        if (typeList.size > 0) return
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData("https://manhua.dmzj.com/tags/category_search/0-0-0-all-0-0-0-1.shtml#category_nav_anchor")
                .collect {
                    what8(it)
                }
        }
    }

    fun getSpeciesData() {
        if (speciesList.size > 0) speciesList.clear()
        Log.i(TAG, "getSpecies:$species")
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData("http://sacg.dmzj.com/mh/index.php?c=category&m=doSearch&status=0&reader_group=0&zone=0&initial=all&type=$species&p=1&callback=search.renderResult")
                .collect {
                    what7(it)
                }
        }
    }

    /**
     * 主页
     * homeFragment
     */

    fun getHomeCartoon(position: Int) {
        pgLiveData.value = false
        val info = cartoonInfors[position]
        var s = info.href
        putBundle(info.titile, info.img, s, R.id.homeFragment)
        Log.i(TAG, "onClick: $s")
        if (s == "") return
        CoroutineScope(Dispatchers.IO).launch {
            if (!s.contains("dmzj"))
                s = Url3 + s
            remote.getData(s).collect {
                what3(it)
            }
        }
    }

    private fun pager() =
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData("https://manhua.dmzj.com/update_$pager.shtml")
                .collect {
                    val document = Jsoup.parse(it)
                    val element = document.getElementsByClass("newpic_content")
                    val elements = element[0].getElementsByClass("boxdiv1")
                    var element1: Element
                    var element2: Element
                    var element3: Element
                    var cartoonInfor: CartoonInfor
                    for (e in elements) {
                        element1 = e.select(".picborder a").first() //图片
                        element2 = e.select(".picborder img").first()
                        element3 = e.select(".pictext li")[2]
                        cartoonInfor = CartoonInfor(
                            element1.attr("title"),
                            element1.attr("href"),
                            element2.attr("src"),
                            element3.text()
                        )
                        cartoonInfors.add(cartoonInfor)
                    }
                    homeLiveData.postValue(cartoonInfors)
                }
        }


    fun nextPager() { //下一页
        pager++
        pager()
    }

    fun refreshPager() { //刷新
        pager = 1
        cartoonInfors.clear()
        pager()
    }

    fun getHomeCartoon() {
        pager()
    }

    /***
     * favouriteFragment
     *
     */
    fun favouriteGet(favouriteInfor: FavouriteInfor) {
        val url = favouriteInfor.url
        Log.i(TAG, "favouriteGet: $url")
        putBundle(
            favouriteInfor.title,
            favouriteInfor.imgUrl,
            favouriteInfor.url,
            R.id.favoriteFragment
        )
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData(url)
                .collect {
                    what3(it)
                }
        }
    }


    /**
     * search
     */
    fun getSearch(position: Int) {
        val cartoonInfor = searchList[position]
        val s = cartoonInfor.href
        putBundle(cartoonInfor.titile, cartoonInfor.img, s, R.id.searchFragment)
        Log.i(TAG, "onClick: $s")
//        NetworkUtils.getInstance().OkhttpGet(handler, s, 3)
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData(s).collect { what3(it) }
        }
    }


    //    public void setSearchFavourite(int position) {
    //        CartoonInfor cartoonInfor = listMsg5.get(position);
    //        cartoonModel.insert(new FavouriteInfor(cartoonInfor.getHref(), cartoonInfor.getImg(), cartoonInfor.getTitile()));
    //    }
    //
    fun search(name: String?) {
        Log.i(TAG, "search: $name")
        CoroutineScope(Dispatchers.IO).launch {
            remote.getData("https://sacg.dmzj.com/comicsum/search.php?s=$name")
                .collect {
                    what5(it)
                }
        }
//        NetworkUtils.getInstance().OkhttpGet(
//            handler,
//            "https://sacg.dmzj.com/comicsum/search.php?s=$name", 5
//        )
    }

    //
    fun clearSearchList() {
        Log.i(TAG, "clearSearchList: ")
        if (searchList.size > 0) searchList.clear()
    }

    /**
     * 逻辑处理部分
     */
    private fun decode(unicodeStr: String?): String? { //UNICODE转中文
        if (unicodeStr == null) {
            return null
        }
        val retBuf = StringBuilder()
        val maxLoop = unicodeStr.length
        var i = 0
        while (i < maxLoop) {
            if (unicodeStr[i] == '\\') {
                if (i < maxLoop - 5 && (unicodeStr[i + 1] == 'u' || unicodeStr[i + 1] == 'U')) try {
                    retBuf.append(unicodeStr.substring(i + 2, i + 6).toInt(16).toChar())
                    i += 5
                } catch (localNumberFormatException: NumberFormatException) {
                    retBuf.append(unicodeStr[i])
                } else retBuf.append(unicodeStr[i])
            } else {
                retBuf.append(unicodeStr[i])
            }
            i++
        }
        return retBuf.toString()
    }

    private fun getStringList(num: Int): String { //有就有，没有就返回原数值
        return if (msg4List[num] == "") {
            //            Log.i(TAG, "getStringList: " + num);
            num.toString()
        } else msg4List[num]
    }

    private fun getStringList2(num: Int): String { //有就有，没有就没有
        return msg4List[num]
    }

    private fun conversion(s: Char): Int {
        var a = 0
        when {
            s.toInt() in 48..57 -> {
                a = s.toInt() - 48 //0-9
            }
            s.toInt() in 65..90 -> {
                a = s.toInt() - 29 //36-61
            }
            s.toInt() in 97..122 -> {
                a = s.toInt() - 87 //10-35
            }
        }
        return a
    }

    private fun conversionString(s: String): Int {
        Log.i(TAG, "conversion: $s")
        var a = 0
        val intS = s.toInt()
        if (intS >= 10) {
            a = intS + 51
        }
        Log.i(TAG, "conversion: $a")
        return a
    }

    private fun twoWords(bj: String, stringBuffer: StringBuilder) { //EG：11 or 1a
        val matcher = Pattern.compile(".*[a-zA-Z]+.*").matcher(bj) //判断有没有包含字母
        if (matcher.matches()) {
            stringBuffer.append(getStringList2(bj[1].toInt() - 25))
        } else {
            val iBj = bj.toInt()
            if (iBj >= 10 && iBj + 52 < msg4List.size) { //66=14 66是第67个
                val sBj = getStringList2(iBj + 52)
                if (sBj != "") {
                    stringBuffer.append(sBj)
                } else {
                    stringBuffer.append(iBj)
                }
            } else {
                stringBuffer.append(iBj)
            }
        }
    }

    private fun split(bj: String, stringBuffer: StringBuilder, mark: String) {
        val bjs = bj.split(mark).toTypedArray()
        var k = 0
        val bjsLength = bjs.size
        while (k < bjsLength) {
            val bj2 = bjs[k]
            when {
                bj2.length == 1 -> {
                    //                                                Log.i(TAG, ": " + bj2.charAt(0));
                    stringBuffer.append(getStringList(conversion(bj2[0])))
                }
                bj2.length == 2 -> {
                    twoWords(bj2, stringBuffer)
                }
                bj.contains(".") -> {
                    val bjs3 = bj2.split(".").toTypedArray()
                    var i = 0
                    val bj3Length = bjs3.size
                    while (i < bj3Length) {
                        val bj3 = bjs3[i]
                        if (bj3.length == 1) stringBuffer.append(getStringList(conversion(bj3[0])))
                        if (bj3.length == 2) twoWords(bj3, stringBuffer)
                        if (i != bj3Length - 1 || i == 0) stringBuffer.append(".")
                        i++
                    }
                }
            }
            if (k != bjsLength - 1 || k == 0) stringBuffer.append(mark)
            k++
        }
    }
}
package com.example.viewModel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.*
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.plus

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

    /**判断是否在searchFragment**/
    var isSearchFragment = false

    //bundle跳转需要传递的数据
    val bundle = Bundle()

    //监听是否隐藏bottom false为显示
    val bottomLiveData = MutableLiveData<Boolean>()

    //加载监听
    val pgLiveData = MutableLiveData<Boolean>()

    //msg2主页漫画
    val cartoonInfors: MutableList<CartoonInfor> = ArrayList()
    val homeLiveData = MutableLiveData<Boolean>()
    private var pager = 1

    //主页57推荐漫画
    private val homeRecommendList by lazy { ArrayList<CartoonInfor>() }
    val homeRecommendLiveData by lazy { MutableLiveData<List<CartoonInfor>>() }

    //msg3集数
    val msg3List: MutableList<CartoonInfor> = ArrayList()
    val msg3LiveData = MutableLiveData<Boolean>()
    var content: String? = null
    var update: String? = null

    //msg4显示漫画
    val msg4List: MutableList<String> = ArrayList()
    private val imgList: MutableList<ByteArray> = ArrayList()
    val msg4LiveData = MutableLiveData<List<ByteArray>>()

    //search
    val searchList: MutableList<CartoonInfor> = ArrayList()
    val searchLiveData = MutableLiveData<Int>()
    val searchList57: MutableList<CartoonInfor> = ArrayList()

    //banner
    val bannerList: MutableList<CartoonInfor> = ArrayList()
    val bannerLiveData = MutableLiveData<List<CartoonInfor>>()

    //remote
    private val remote = CartoonRemote()
    val errorLiveData = remote.error

    //    private val remote = CartoonRemote(errorLiveData)
    private suspend fun what3(string: String) {//集数
        val document = Jsoup.parse(string)
        val elements = document.getElementsByClass("cartoon_online_border")
//        Log.i(TAG, "what3: ${document}")
        if (elements.text().isEmpty()) {
            pgLiveData.postValue(true)
            errorLiveData.postValue("此漫画无法浏览")
            return
        }
        update = document.getElementsByClass("update2").text() ?: ""
        content = document.select(".line_height_content").text()
        val elements1 = elements.select("a")
        var cartoonInfor: CartoonInfor
        for (e in elements1) {
            cartoonInfor = CartoonInfor(e.text(), e.attr("href"))
            msg3List.add(cartoonInfor)
        }
        if (msg3List.size > 0) msg3LiveData.postValue(null)
        delay(300)
        pgLiveData.postValue(true)
        return
    }

    private suspend fun what357(string: String) {
        val document = Jsoup.parse(string)
        val elements = document.getElementsByClass("chapter-list cf mt10")
        if (elements.text().isEmpty()) {
            pgLiveData.postValue(true)
            errorLiveData.postValue("此漫画无法浏览")
            return
        }
        content = document.select("#intro-cut").text()
        val elements1 = elements.select("li")
        var cartoonInfor: CartoonInfor
        for (e in elements1) {
            cartoonInfor = CartoonInfor(e.text(), Api.mh57Url + e.selectFirst("a").attr("href"))
            msg3List.add(cartoonInfor)
        }
        if (msg3List.size > 0) {
            msg3List.reverse()
            msg3LiveData.postValue(null)
        }
        delay(300)
        pgLiveData.postValue(true)
        return
    }

    private suspend fun what157(string: String) {
        val document = Jsoup.parse(string).body()
        val elements = document.getElementsByTag("script")
        if (elements.isEmpty()) {
            pgLiveData.postValue(true)
            errorLiveData.postValue("所选漫画消失")
            return
        }
        val s = elements[5].data()
        Log.i(TAG, "what157: ${elements[5]}")
        msg4List.addAll(s.substring(s.lastIndexOf(",'") + 2, s.indexOf("'.split")).split("|"))
        val strings = s.substring(s.indexOf(":[") + 2, s.indexOf("],")).split(",").toTypedArray()
        var startUrl: String? = null
        for (str in strings) {
            if (!job!!.isActive) return
            val string2 =
                str.substring(str.indexOf("\\'/") + 3, str.lastIndexOf("\\'"))
            Log.i(TAG, "what157: $string2")
            val stringBuilder = StringBuilder()
            if (startUrl == null) {
                startUrl = if (string2.contains("://"))
                    getStringList(conversion(string2[0])) + ":/"
                else
                    Api.img57Url
            }
            stringBuilder.append(startUrl)
            for (s2 in string2.substring(4).split("/")) {
                when {
                    s2.contains("-") -> {
                        stringBuilder.append("/")
                        split(s2, stringBuilder, "-")
                    }
                    s2.contains(".") -> {
                        stringBuilder.append("/")
                        split(s2, stringBuilder, ".")
                    }
                    s2.contains("~") -> {
                        stringBuilder.append("/")
                        split(s2, stringBuilder, "~")
                    }
                    else -> {
                        stringBuilder.append("/").append(getStringList(conversion(s2[0])))
                    }
                }
            }
            send(stringBuilder.toString())
        }
        loadImg()
    }

    //老代码不想优化
    private suspend fun what1(string: String) {//图片
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
                if (!job!!.isActive)
                    return
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
                    val sss0 = if (sss[0].isNotEmpty()) {
                        val c = sss[0][0]
                        if (conversion(c) >= msg4List.size) c.toString() else getStringList2(
                            conversion(
                                c
                            )
                        )
                    } else {
                        ""
                    }

                    if (sss0 == "") //判断*/y/*的y
                        stringBuffer.append(Api.imgUrl).append(sss[0])
                            .append("/") else stringBuffer.append(Api.imgUrl).append(sss0)
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
                                        when {
                                            ss0.length == 1 -> stringBuffer.append(
                                                getStringList(
                                                    conversion(
                                                        ss0[0]
                                                    )
                                                )
                                            )
                                            ss0.contains("-") -> {
                                                split(ss0, stringBuffer, "-")
                                            }
                                            else -> stringBuffer.append(
                                                getStringList(
                                                    conversionString(
                                                        ss0
                                                    )
                                                )
                                            )
                                        }
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
                                when {
                                    ssss0.contains(".") -> {
                                        stringBuffer.append(ssss0)
                                    }
                                    ssss0.contains("-") -> {
                                        split(ssss0, stringBuffer, "-")
                                    }
                                    else -> {
                                        stringBuffer.append(getStringList(conversion(ssss0[0])))
                                    }
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
                                                    ssssk[0]
                                                )
                                            )
                                        )
                                    }
                                    ssssk.contains(".") -> {
                                        stringBuffer.apply {
                                            append("%")
                                            append(
                                                getStringList(
                                                    conversion(
                                                        ssssk[0]
                                                    )
                                                )
                                            )
                                            append(".")
                                            if (ssssk.length == 3)
                                                stringBuffer.append(getStringList(conversion(ssssk[2])))
                                        }

                                    }
                                    ssssk.contains("-") -> {
                                        stringBuffer.apply {
                                            append("%")
                                            append(
                                                getStringList(
                                                    conversion(
                                                        ssssk[0]
                                                    )
                                                )
                                            )
                                            append("-")
                                            if (ssssk.length == 3)
                                                stringBuffer.append(getStringList(conversion(ssssk[2])))
                                        }

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
                                            Log.i(TAG, "what1:bj.contains(-) ")
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
            pgLiveData.postValue(true)
            errorLiveData.postValue("所选漫画消失")
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
            if (searchJob.isActive)
                searchLiveData.postValue(1)
        } else {
            errorLiveData.postValue("动漫之家查询不到此漫画")
        }
    }

    /***加载漫画,判断漫画源**/
    private fun loadCartoon(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (url.contains("wuqimh")) {
                remote.getData(url) {
                    pgLiveData.postValue(true)
                }.collect {
                    what357(it)
                }
                return@launch
            }
            var s = url
            if (!s.contains("dmzj"))
                s = Api.url2 + "/" + s//需要加"/"
            remote.getData(s) { pgLiveData.postValue(true) }
                .collect {
                    Log.i(TAG, "getHomeCartoon: $s")
                    what3(it)
                }
        }
    }


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
        Log.i(TAG, "what7: $str2")
        if (!str2.contains("search.renderResult")) {
            errorLiveData.postValue("数据加载失败")
            return
        }
        adapterTopLiveData.postValue(true)
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


    /**
     * 漫画本月人气排行
     */
    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.url2 + "/rank/month-block-1.shtml")
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
        val url = msg3List[position].href
        job = CoroutineScope(Dispatchers.IO).launch {
            if (url.contains("wuqimh")) {
                remote.getData(url) {
                    pgLiveData.postValue(true)
                }.collect {
                    what157(it)
                }
            } else {
                remote.getData(Api.url2 + url)
                {
                    pgLiveData.postValue(true)
                }.collect {
                    what1(it)
                }
            }
        }
    }

    fun onMsg3Dismiss() { //清除集数
        msg3List.clear()
        if (job != null)
            if (job!!.isActive) {
                job!!.cancel()
                pgLiveData.postValue(true)
                if (imgList.size > 0) imgList.clear()
            }
        Log.i("TAG", "onDismiss3: ")
    }

    fun onMsg4Dismiss() {
        job?.cancel()
        if (imgList.size > 0) imgList.clear()
        Log.i("TAG", "onDismiss4: ")
    }

    private var job: Job? = null
    val imgUrlList = mutableListOf<String>()
    private suspend fun loadImg() {
        for (url in imgUrlList) {
            if (!job!!.isActive) break
            remote.getImg(url).collect {
                if (job!!.isActive)
                    imgList.add(it!!)
                if (job!!.isActive)
                    msg4LiveData.postValue(imgList)
            }
        }
        if (imgUrlList.size > 0) imgUrlList.clear()
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
        if (typeList.size > 0) {//判断是否有加载过分类，有就加载现有数据
            speciesLiveData.value = true
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.url2 + "/tags/category_search/0-0-0-all-0-0-0-1.shtml#category_nav_anchor")
                .collect {
                    what8(it)
                }
        }
    }

    val adapterTopLiveData = MutableLiveData<Boolean>()//分类切换监听有没有成功 成功就变色
    fun getSpeciesData() {
        if (speciesList.size > 0) speciesList.clear()
        Log.i(TAG, "getSpecies:$species")
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(
                Api.sacgUrl + "mh/index.php?c=category&m=doSearch&status=0" +
                        "&reader_group=0&zone=0&initial=all&type=$species&p=1&callback=search.renderResult"
            )
                .collect {
                    what7(it)
                }
        }
    }

    fun getSpeciesCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = speciesList[position]
        val s = info.url
        putBundle(info.title, info.imgUrl, s, R.id.speciesFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        loadCartoon(s)

    }

    /**
     * 主页
     * homeFragment
     */

    //获取漫画详细
    fun getHomeCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = cartoonInfors[position]
        val s = info.href
        putBundle(info.titile, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        loadCartoon(s)
    }

    //获取漫画页面
    private fun pager() =
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.url2 + "/update_$pager.shtml") {//需要加"/"
                errorLiveData.postValue(null)
            }.collect {
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
                homeLiveData.postValue(null)
            }
        }


    //推荐
    fun get57Recommend() {
        viewModelScope.launch(Dispatchers.IO) {
            remote.getData(Api.mh57Url)
                .collect {
                    val document = Jsoup.parse(it)
                    val element: Element = document.select(".update-wrap").first()
                    for (e in element.select("li")) {
                        val a = e.select("a").first()
                        val img = a.select("img").first()
                        var src = img.attr("src")
                        if (src.isEmpty()) src = img.attr("data-src")
                        homeRecommendList.add(
                            CartoonInfor(
                                a.attr("title"),
                                Api.mh57Url + a.attr("href"),
                                src
                            )
                        )
                    }
                    homeRecommendLiveData.postValue(homeRecommendList)
                    Log.i(TAG, "get57Recommend: $element")
                }
        }
    }

    //获取推荐漫画详细
    fun getHomeRecommendCartoon(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val info = homeRecommendList[position]
        val s = info.href
        Log.i(TAG, "getHomeRecommendCartoon: ${s + info.img}")
        putBundle(info.titile, info.img, s, R.id.homeFragment)
        if (s.isEmpty()) {
            pgLiveData.value = true
            return
        }
        loadCartoon(s)
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
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = favouriteInfor.url
        Log.i(TAG, "favouriteGet: $url")
        putBundle(
            favouriteInfor.title,
            favouriteInfor.imgUrl,
            url,
            R.id.favoriteFragment
        )
        loadCartoon(url)

    }

    /**
     *historyFragment
     * */
    fun historyGet(historyInfor: HistoryInfor) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val url = historyInfor.href
        Log.i(TAG, "historyGet: $url")
        putBundle(
            historyInfor.title,
            historyInfor.imgUrl,
            url,
            R.id.historyFragment
        )

        loadCartoon(url)

    }

    /**
     * search
     */
    fun getSearch(position: Int) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val cartoonInfor = searchList[position]
        val s = cartoonInfor.href
        putBundle(cartoonInfor.titile, cartoonInfor.img, s, R.id.searchFragment)
        loadCartoon(s)
    }

    fun getSearch57(cartoonInfor: CartoonInfor) {
        if (pgLiveData.value == false) return
        pgLiveData.value = false
        val s = Api.mh57Url + cartoonInfor.href
        putBundle(cartoonInfor.titile, cartoonInfor.img, s, R.id.searchFragment)
        loadCartoon(s)
    }

    private lateinit var searchJob: Job
    fun search(name: String?) {
        pgLiveData.value = false
        Log.i(TAG, "search: $name")
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            launch {
                remote.getData(Api.sacgUrl + "comicsum/search.php?s=$name")
                    .collect {
                        what5(it)
                    }
            }
            launch {
                remote.getData(Api.mh57Url + "/search/q_$name").collect {
                    mh57Search(it)
                }
            }
        }
    }


    fun clearSearchList() {
        Log.i(TAG, "clearSearchList: ")
        searchJob.cancel()
        if (searchList.size > 0) searchList.clear()
        if (searchList57.size > 0) searchList57.clear()
    }

    private fun mh57Search(string: String) {
        val jsoup = Jsoup.parse(string)
        val elements = jsoup.getElementsByClass("cf")
        if (elements.isEmpty()) {
            errorLiveData.postValue("57漫画查询不到此漫画")
            return
        }
        for (i in 2 until elements.size) {
            val e = elements[i]
            val a = e.select(".bcover")
            if (a.isEmpty())
                continue
            searchList57.add(
                CartoonInfor(
                    a.attr("title"),
                    a.attr("href"),
                    a.select("img").attr("src")
                )
            )
        }
        if (searchJob.isActive)
            searchLiveData.postValue(2)
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
        return if (msg4List[num].isEmpty()) {
//            Log.i(TAG, "getStringList: $num")
            getStringListChar.toString()
        } else msg4List[num]
    }

    private fun getStringList2(num: Int): String { //有就有，没有就没有
        return msg4List[num]
    }

    private fun conversion(s: Char): Int {
        var a = 0
        getStringListChar = s
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
        getStringListChar = s[0]
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

    /**保存传入的字符，用于list对应为空时返回原字符**/
    private var getStringListChar: Char = 'a'
    private fun split(bj: String, stringBuffer: StringBuilder, mark: String) {
        val bjs = bj.split(mark).toTypedArray()
        for (b in bjs.indices) {
            val bj2 = bjs[b]
//            Log.i(TAG, "split:bj2$bj2 ")
            when {
                bj2.contains(".") -> {
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
                bj2.length == 1 -> {
                    //Log.i(TAG, ": " + bj2.charAt(0));
                    stringBuffer.append(getStringList(conversion(bj2[0])))
                }
                bj2.length == 2 -> {
                    twoWords(bj2, stringBuffer)
                }

            }
            if (b != bjs.size - 1 || b == 0) stringBuffer.append(mark)
        }
    }
}
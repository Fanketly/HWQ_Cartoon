package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.util.TAG
import com.example.repository.model.CartoonInfo
import com.example.repository.model.KBCartoonChapter
import com.example.repository.remote.Api
import com.example.repository.remote.CartoonRemote
import com.example.util.NetworkUtils
import com.example.repository.remote.RequestUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.plus

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/23
 * Time: 21:16
 */
class DetailViewModel @ViewModelInject constructor(
    private val requestUtil: RequestUtil,
    private val remote: CartoonRemote
) : ViewModel() {

    val content: String?
        get() = requestUtil.content
    val update: String?
        get() = requestUtil.update

    //msg3集数
    val msg3List: MutableList<CartoonInfo> by lazy { requestUtil.msg3List }

    //    private val remote = CartoonRemote
    val pgLiveData = remote.pgLiveData
    private val errorLiveData = remote.error
    val bottomLiveData = remote.bottomLiveData

    //msg4显示漫画
    private val msg4List: MutableList<String> by lazy { ArrayList() }
    private var job: Job? = null
    private val imgUrlList = ArrayList<String>()
    val msg4LiveData by lazy { MutableLiveData<List<String>>() }


    /**
     * 加载图片部分
     */
    fun msg3Send(position: Int) {
        //判断解析数据是否清除
        if (msg4List.size > 0) msg4List.clear()
        pgLiveData.value = false
        val url = msg3List[position].href
        Log.i(TAG, "msg3Send: $url")
        job = CoroutineScope(Dispatchers.IO).launch {
            when {
                url.contains("ykmh") -> {
                    remote.getData<String>(url, data = { data, flow ->
                        val document = Jsoup.parse(data).body()
                        val elements = document.getElementsByTag("script")
                        if (elements.isEmpty()) {
                            pgLiveData.postValue(true)
                            errorLiveData.postValue("所选漫画消失")
                        } else {
                            val s = elements[0].data()
                            val ss = s.substring(s.indexOf("[") + 1, s.indexOf("]")).split(",")
                            for (str in ss) {
                                flow.emit(str)
                            }
                        }
                    }, success = {
                        msg4LiveData.postValue(imgUrlList)
                    }).collect {
                        send(Api.imgYKUrl + it.replace("\\", "").replace("\"", ""))
                    }
                }
                url.contains("copymanga") -> {
                    val data =
                        remote.getData(url, KBCartoonChapter::class.java, requestUtil.headers)
                            ?: return@launch
                    if (data.code == 200) {
                        val words = data.results.chapter.words.toMutableList()
                        val contents = data.results.chapter.contents.toMutableList()
                        quickSort(words, contents, 0, words.size - 1)
                        Log.i(TAG, "msg3Send: $words")
                        for (content in contents) {
                            if (!job!!.isActive) {
                                return@launch
                            }
                            send(content.url)
                        }
                        msg4LiveData.postValue(imgUrlList)
                    } else {
                        errorLiveData.postValue(data.message)
                    }
                }
                else -> {
                    what1(Api.url2 + url)
                }
            }
        }
    }

    private fun quickSort(
        words: MutableList<Int>,
        contents: MutableList<KBCartoonChapter.Results.Chapter.Content>,
        begin: Int,
        end: Int
    ) {
        if (begin < end) {
            val temp = words[begin]
            val temp2 = contents[begin]
            var i = begin
            var j = end
            while (i < j) {
                while (i < j && words[j] > temp)
                    j--
                words[i] = words[j]
                contents[i] = contents[j]
                while (i < j && words[i] <= temp)
                    i++
                words[j] = words[i]
                contents[j] = contents[i]
            }
            words[i] = temp
            contents[i] = temp2
            quickSort(words, contents, begin, i - 1)
            quickSort(words, contents, i + 1, end)
        } else {
            return
        }
    }

    private fun send(url: String) {//what4
        imgUrlList.add(url)
    }

    //老代码不想优化
    private fun what1(url: String) {//图片
        val string = NetworkUtils.okhttpGet(url)
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
            msg4LiveData.postValue(imgUrlList)
        } else {
            pgLiveData.postValue(true)
            errorLiveData.postValue("所选漫画消失")
        }
    }


    fun onMsg3Dismiss() { //清除集数
        msg3List.clear()
        job?.run {
            if (isActive){
                cancel()
                pgLiveData.postValue(true)
            }
            Log.i("TAG", "onDismiss3: ")
        }
    }

    fun onMsg4Dismiss() {
        job?.cancel()
        //清除解析数据
        msg4List.clear()
        if (imgUrlList.size > 0) imgUrlList.clear()
    }

    /**
     * 逻辑处理部分
     */
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
        when (s.code) {
            in 48..57 -> {
                a = s.code - 48 //0-9
            }
            in 65..90 -> {
                a = s.code - 29 //36-61
            }
            in 97..122 -> {
                a = s.code - 87 //10-35
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
            stringBuffer.append(getStringList2(bj[1].code - 25))
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
package com.example.repository.remote

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.repository.model.CartoonInfo
import com.example.repository.model.KBCartoonChapters
import com.example.repository.model.KBCartoonInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Headers
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/4/1
 * Time: 21:16
 */
@Singleton
class RequestUtil @Inject constructor(val remote: CartoonRemote) {
    //msg3集数
    val msg3List: MutableList<CartoonInfo> by lazy { ArrayList() }
    private val msg3liveData by lazy { MutableLiveData<Bundle?>() }
    val msg3LiveData
        get() = msg3liveData
    var content: String? = null
    var update: String? = null

    //remote
    private val errorLiveData = remote.error
    private val pgLiveData = remote.pgLiveData//加载监听
    val headers = Headers.headersOf(
        "referer",
        "https://api.copymanga.com",
        "User-Agent",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36 Edg/86.0.622.38",
        "source",
        "copyApp"
    )

    //跳转传递数据
    private val bundle by lazy { Bundle() }
    fun putBundle(name: String, img: String, href: String, mark: Int) {
        if (bundle.size() > 0)
            bundle.clear()
        bundle.putString("name", name)
        bundle.putString("img", img)
        bundle.putString("href", href)
        bundle.putInt("mark", mark)
    }

    suspend fun <T> getFlow(
        list: List<T>,
        collect: suspend (data: T) -> Unit,
        success: (suspend () -> Unit)? = null,
        fail: (suspend () -> Unit)? = null
    ) {
        list.asFlow().onCompletion { cause -> if (cause == null) success?.invoke() }
            .catch {
                errorLiveData.postValue(it.message)
                pgLiveData.postValue(true)
                fail?.invoke()
            }.collect { collect(it) }
    }

    /***加载漫画,判断漫画源**/
    fun loadCartoon(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when {
                url.contains("ykmh") -> {
                    Log.i("TAG", "RequestUtil_loadYkCartoon:$url ")
                    remote.getData<CartoonInfo>(url, data = { data, flow ->
                        val document = Jsoup.parse(data)
                        val elements = document.getElementsByClass("list_con_li autoHeight")
                        if (elements.text().isEmpty()) {
                            pgLiveData.postValue(true)
                            errorLiveData.postValue("此漫画无法浏览")
                            return@getData
                        }
                        content = document.select(".comic_deCon_d").text()
                        for (e in elements.select("li")) {
                            flow.emit(
                                CartoonInfo(
                                    e.selectFirst(".list_con_zj").text(),
                                    Api.youkuUrl + e.selectFirst("a").attr("href")
                                )
                            )
                        }

                    }, success = {
                        msg3liveData.postValue(bundle)
                        delay(300)
                        pgLiveData.postValue(true)
                    }).collect {
                        msg3List.add(it)
                    }

                }
                url.contains("copymanga") -> {
                    val data =
                        remote.getData(url, KBCartoonInfo::class.java, headers) ?: return@launch
                    if (data.code == 200) {
                        val results = data.results
                        val comic = results.comic
                        val default = results.groups.default
                        val data1 = remote.getData(
                            Api.baseKbUrl + "comic/${comic.pathWord}/group/${default.pathWord}/chapters?limit=${default.count}&amp;offset=0&amp;platform=3",
                            KBCartoonChapters::class.java,
                            headers
                        ) ?: return@launch
                        if (data1.code == 200) {
                            content = comic.brief
                            update = comic.datetimeUpdated
                            for (chapter in data1.results.list) {
                                msg3List.add(
                                    CartoonInfo(
                                        chapter.name,
                                        Api.baseKbUrl + "comic/${chapter.comicPathWord}/chapter2/${chapter.uuid}?platform=3"
                                    )
                                )
                            }
                            if (msg3List.size > 0) msg3liveData.postValue(bundle)
                            delay(300)
                            pgLiveData.postValue(true)
                        } else {
                            errorLiveData.postValue(data1.message)
                        }
                    } else {
                        errorLiveData.postValue(data.message)
                    }
                }
                else -> {
                    var s = url
                    if (!s.contains("dmzj"))
                        s = Api.url2 + "/" + s//需要加"/"
                    Log.i("TAG", "RequestUtil_loadCartoon:$s ")
                    remote.getData<CartoonInfo>(s, data = { data, flow ->
                        val document = Jsoup.parse(data)
                        val elements = document.getElementsByClass("cartoon_online_border")
                        if (elements.text().isEmpty()) {
                            pgLiveData.postValue(true)
                            errorLiveData.postValue("此漫画无法浏览")
                            return@getData
                        }
                        update = document.getElementsByClass("update2").text() ?: ""
                        content = document.select(".line_height_content").text()
                        val elements1 = elements.select("a")
                        for (e in elements1) {
                            flow.emit(
                                CartoonInfo(
                                    e.text(),
                                    e.attr("href")
                                )
                            )
                        }

                    }, success = {
                        if (msg3List.size > 0) msg3liveData.postValue(bundle)
                        delay(300)
                        pgLiveData.postValue(true)
                    }).collect {
                        msg3List.add(it)
                    }
                }
            }
        }
    }


}
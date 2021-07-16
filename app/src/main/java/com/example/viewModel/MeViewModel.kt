package com.example.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.hwq_cartoon.AUTO
import com.example.hwq_cartoon.THEME
import com.example.hwq_cartoon.App
import com.example.hwq_cartoon.R
import com.example.repository.model.SettingInfo
import com.example.repository.remote.CartoonRemote

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/4/6
 * Time: 14:12
 */

class MeViewModel @ViewModelInject constructor(
    remote: CartoonRemote
) : ViewModel() {
    init {
        Log.i("TAG", "me: ")
    }

    val bottomLiveData = remote.bottomLiveData

    val settingList = mutableListOf(
        SettingInfo(R.drawable.auto, "漫画自动滚动速度设置"),
        SettingInfo(R.drawable.liuyan, "留言反馈"),
        SettingInfo(R.drawable.auto, "漫画阅读模式设置"),
        SettingInfo(R.drawable.about, "关于我们")
    )

    fun saveAuto(i: Int) {
        App.kv.encode(AUTO, i)
        App.autoSetting = i
    }

    fun selectTheme() {
        App.blackTheme = !App.blackTheme
        App.kv.encode(THEME, App.blackTheme)
    }

}
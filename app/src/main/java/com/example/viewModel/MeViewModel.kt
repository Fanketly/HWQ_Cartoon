package com.example.viewModel

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.example.base.AUTO
import com.example.hwq_cartoon.App
import com.example.hwq_cartoon.R
import com.example.repository.model.SettingInfo

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/4/6
 * Time: 14:12
 */
class MeViewModel : ViewModel() {
    init {
        Log.i("TAG", "me: ")
    }


    val settingList = mutableListOf(
        SettingInfo(R.drawable.auto, "漫画自动滚动速度设置"),
        SettingInfo(R.drawable.auto, "漫画自动滚动速度设置"), SettingInfo(R.drawable.auto, "漫画自动滚动速度设置"),
        SettingInfo(R.drawable.auto, "漫画自动滚动速度设置"), SettingInfo(R.drawable.auto, "漫画自动滚动速度设置")
    )

    suspend fun saveAuto(dataStore: DataStore<Preferences>, i: Int) {
        dataStore.edit {
            it[AUTO] = i
            App.autoSetting = i
        }
    }

}
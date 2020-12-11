package com.example.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/10
 * Time: 13:55
 */
abstract class BaseFragment(@LayoutRes val layId :Int) : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layId, container, false)
    }
}
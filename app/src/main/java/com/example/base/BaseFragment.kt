package com.example.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/10
 * Time: 13:55
 */

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layId: Int) : Fragment() {
    lateinit var b: T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        b = DataBindingUtil.inflate(inflater, layId, container, false)
        b.lifecycleOwner = this
        return b.root
    }


    private var toast: Toast? = null
    fun shortToast(string: String) {
        toast?.cancel()
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun longToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}
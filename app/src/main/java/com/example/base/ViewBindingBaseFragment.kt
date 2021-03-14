package com.example.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.hwq_cartoon.databinding.FragmentMeBinding

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/3/14
 * Time: 16:52
 */
abstract class ViewBindingBaseFragment<T : ViewBinding>() : Fragment() {
    inline fun <reified VB : T> viewBinding(layoutInflater: LayoutInflater): VB {
        return VB::class.java.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB
    }

    private var _b: T? = null
    val b get() = _b!!

    /**
     * 绑定视图
     * **/
    abstract fun rootView(): T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _b = rootView()
        return b.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
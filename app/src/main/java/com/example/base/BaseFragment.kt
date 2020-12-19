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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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
        b = DataBindingUtil.inflate(LayoutInflater.from(context), layId, container, false)
        return b.root
    }

    inline fun <reified V : ViewModel> viewModel(clazz: Class<out ViewModel>): V {
        return ViewModelProvider(requireActivity())[clazz] as V
    }

//    fun beginTransaction(bundle: Bundle?, clazz: Class<out Fragment>, layId: Int) =
//        requireActivity().supportFragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
//            .add(layId, clazz, bundle, "detail").commit()


    fun shortToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    fun longToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}
package com.example.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.detailed.DetailedFragment
import com.example.hwq_cartoon.R

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/10
 * Time: 13:55
 */
abstract class BaseFragment(@LayoutRes val layId: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layId, container, false)
    }

    fun beginTransaction(bundle: Bundle, layId: Int) =
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_in,R.anim.right_out)
            .add(layId, DetailedFragment::class.java, bundle,"detail").commit()


    fun shortToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    fun longToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}
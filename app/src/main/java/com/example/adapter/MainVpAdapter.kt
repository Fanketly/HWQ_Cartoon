package com.example.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/5/8
 * Time: 13:38
 */
class MainVpAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val list: List<Fragment>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {

        return list[position]
    }
}
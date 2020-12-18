package com.example.favourite

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.base.BaseFragment

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/12/15
 * Time: 10:47
 */
class FavouritevpAdapter(fragment: Fragment, private val fragments:List<Fragment>) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =
        fragments.size


    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}
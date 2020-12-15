package com.example.favourite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.base.BaseFragment
import com.example.hwq_cartoon.R
import com.example.viewModel.FavouriteViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_vp.*

class FavouriteVpFragment : BaseFragment(R.layout.fragment_vp) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewMode=ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = listOf(FavoriteFragment(), HistoryFragment())
        val favouritevpAdapter = FavouritevpAdapter(this, list)
        vpFavourite.offscreenPageLimit = 2
        vpFavourite.adapter = favouritevpAdapter
        tabFavourite.addTab(tabFavourite.newTab().setText("追漫"))
        tabFavourite.addTab(tabFavourite.newTab().setText("历史"))
        viewMode.tabLayLiveData.observe(viewLifecycleOwner){
            if (it)tabFavourite.visibility=View.GONE
            else tabFavourite.visibility=View.VISIBLE
        }
        tabFavourite.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                vpFavourite.currentItem=tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        vpFavourite.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabFavourite.setScrollPosition(position,0f,false)
            }
        })
    }
}
package com.example.favourite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentVpBinding
import com.example.viewModel.FavouriteViewModel
import com.google.android.material.tabs.TabLayout

class FavouriteVpFragment : BaseFragment<FragmentVpBinding>(R.layout.fragment_vp) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewMode = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = listOf(FavoriteFragment(), HistoryFragment())
        val favouritevpAdapter = FavouritevpAdapter(this, list)
        b.vpFavourite.offscreenPageLimit = 2
        b.vpFavourite.adapter = favouritevpAdapter
        b.tabFavourite.addTab(b.tabFavourite.newTab().setText("追漫"))
        b.tabFavourite.addTab(b.tabFavourite.newTab().setText("历史"))
        viewMode.tabLayLiveData.observe(viewLifecycleOwner) {
            if (it) b.tabFavourite.visibility = View.INVISIBLE
            else b.tabFavourite.visibility = View.VISIBLE
        }
        b.tabFavourite.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                b.vpFavourite.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        b.vpFavourite.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                b.tabFavourite.setScrollPosition(position, 0f, false)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "FavVponDestroy: ")
    }
}
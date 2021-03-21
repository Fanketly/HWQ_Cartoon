package com.example.ui.favourite

import android.app.AlertDialog
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
    private lateinit var viewModel: FavouriteViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = listOf(FavoriteFragment(), HistoryFragment())
        val favouritevpAdapter = FavouritevpAdapter(this, list)
        b.vpFavourite.offscreenPageLimit = 2
        b.vpFavourite.adapter = favouritevpAdapter
        b.tabFavourite.addTab(b.tabFavourite.newTab().setText("追漫"))
        b.tabFavourite.addTab(b.tabFavourite.newTab().setText("历史"))
        b.tvVpFavouriteClear.setOnClickListener {
            AlertDialog.Builder(context).setMessage("是否清空历史")
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    viewModel.historyClear()
                }
                .setNegativeButton("取消") { p0, _ ->
                    p0.dismiss()
                }.show()
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
//                b.tabFavourite.setScrollPosition(position, 0f, false)
                b.tabFavourite.getTabAt(position)?.select()
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i(TAG, "onHiddenChanged: $hidden")
        if (!hidden) {
            viewModel.likesIsZero()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "FavVponDestroy: ")
    }
}
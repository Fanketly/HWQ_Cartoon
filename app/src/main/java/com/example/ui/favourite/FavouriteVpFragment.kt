package com.example.ui.favourite

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.base.BaseFragment
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.databinding.FragmentVpBinding
import com.example.viewModel.FavouriteViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteVpFragment : BaseFragment<FragmentVpBinding>() {
    private val viewModel: FavouriteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "FavouriteVpFragment_onViewCreated: ")
        val favouriteVpAdapter = FavouriteVpAdapter(
            this,
            listOf(FavoriteFragment(), HistoryFragment())
        )
        b.vpFavourite.offscreenPageLimit = 1
        b.vpFavourite.adapter = favouriteVpAdapter
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
                b.tabFavourite.getTabAt(position)?.select()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "FavVponDestroy: ")
    }

    override fun viewBinding(container: ViewGroup?): FragmentVpBinding {
        return FragmentVpBinding.inflate(layoutInflater, container, false)
    }
}
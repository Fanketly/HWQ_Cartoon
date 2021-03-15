package com.example.ui.favourite

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.hwq_cartoon.R
import com.example.adapter.FavouriteRvAdapter
import com.example.hwq_cartoon.databinding.FragmentFavoriteBinding
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {
    private var favouriteRvAdapter: FavouriteRvAdapter? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        val favouriteViewModel =
            ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = favouriteViewModel.favouriteList
        favouriteViewModel.likesIsZero()
        favouriteViewModel.likesLiveData.observe(viewLifecycleOwner) {
            if (it) b.tvFavouriteTip.visibility = View.VISIBLE
            else b.tvFavouriteTip.visibility = View.GONE
        }
        if (favouriteRvAdapter == null)
            favouriteRvAdapter = FavouriteRvAdapter(list)
        b.rvFavourite.setUpWithGrid(favouriteRvAdapter, 3)
        b.rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        //漫画点击监听
        favouriteRvAdapter?.setOnClick(onclick = {
            viewModel.favouriteGet(list[it])
        }, longOnclick = {
            AlertDialog.Builder(context).setMessage("是否取消追漫")
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    favouriteViewModel.favouriteDel(it)
                }
                .setNegativeButton("取消") { p0, _ ->
                    p0.dismiss()
                }.show()
        })
        //漫画rv监听
        favouriteViewModel.favouriteLivaData.observe(viewLifecycleOwner) {
            if (favouriteViewModel.delOrIns) {
                favouriteRvAdapter?.notifyItemRemoved(it)
                favouriteRvAdapter?.notifyItemRangeChanged(it, list.size)
            } else {
                favouriteRvAdapter?.notifyItemChanged(it)
            }
        }
    }

}
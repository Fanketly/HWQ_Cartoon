package com.example.favourite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.hwq_cartoon.R
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
        if (list.size > 0)
            b.tvFavouriteTip.visibility = View.GONE
        if (favouriteRvAdapter == null)
            favouriteRvAdapter = FavouriteRvAdapter(list, context)

        b.rvFavourite.setUpWithGrid(favouriteRvAdapter, 3)
        b.rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        //漫画点击监听
        favouriteRvAdapter?.setOnClick { position ->
            viewModel.favouriteGet(list[position])
            favouriteViewModel.tabLayLiveData.value = true
        }
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
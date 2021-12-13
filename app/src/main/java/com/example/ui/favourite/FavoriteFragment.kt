package com.example.ui.favourite

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.FavouriteRvAdapter
import com.example.util.setUpWithGrid
import com.example.hwq_cartoon.databinding.FragmentFavoriteBinding
import com.example.viewModel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var favouriteRvAdapter: FavouriteRvAdapter? = null
    private var b: FragmentFavoriteBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return b!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favouriteViewModel =
            ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = favouriteViewModel.favouriteList
        favouriteViewModel.likesIsZero()
        b?.run {
            favouriteViewModel.likesLiveData.observe(viewLifecycleOwner) {
                if (it) tvFavouriteTip.visibility = View.VISIBLE
                else tvFavouriteTip.visibility = View.GONE
            }
            favouriteRvAdapter = FavouriteRvAdapter(list)
            favouriteRvAdapter?.run {
                rvFavourite.setUpWithGrid(this, 3)
                rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                //漫画点击监听
                setOnClick(onclick = {
                    favouriteViewModel.favouriteGet(list[it])
                }, longOnclick = {
                    AlertDialog.Builder(context).setMessage("是否取消追漫")
                        .setPositiveButton(
                            "确定"
                        ) { _, _ ->
                            favouriteViewModel.favouriteDel(it)
                            favouriteViewModel.likesIsZero()
                        }
                        .setNegativeButton("取消") { p0, _ ->
                            p0.dismiss()
                        }.show()
                })
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

    override fun onDestroyView() {
        super.onDestroyView()
        b = null
    }

}
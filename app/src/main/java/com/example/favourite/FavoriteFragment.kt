package com.example.favourite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private var favouriteRvAdapter: FavouriteRvAdapter? = null

    //需要传递的数据
    private var name = ""
    private var img = ""

    //    private var position = 0
    private var mark = R.id.homeFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        val favouriteViewModel =
            ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val list = favouriteViewModel.favouriteList
        if (list.size > 0)
            tvFavouriteTip.visibility = View.GONE
        if (favouriteRvAdapter == null)
            favouriteRvAdapter = FavouriteRvAdapter(list, R.layout.rv_item_favourite, context)

        rvFavourite.setUpWithGrid(favouriteRvAdapter, 3)
        rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        favouriteRvAdapter?.setOnClick { position ->
            val favouriteInfor = list[position]
            viewModel.favouriteGet(favouriteInfor.url)
            name = favouriteInfor.title
            img = favouriteInfor.imgUrl
        }
        //msg3集数
        viewModel.liveDataMsg3.observe(viewLifecycleOwner, { msg3: List<CartoonInfor?> ->
            if (msg3.isNotEmpty()) {
                Log.i("TAG", "msg3: ")
                val bundle = Bundle()
                bundle.putString("name", name)
                bundle.putString("img", img)
                bundle.putInt("mark", mark)
                viewModel.bottomLiveData.value = true
                favouriteViewModel.tabLayLiveData.value = true
                beginTransaction(bundle, R.id.layFavourite)
            }
        })
        favouriteViewModel.favouriteLivaData.observe(viewLifecycleOwner){
            if (favouriteViewModel.delOrIns) {
                favouriteRvAdapter?.notifyItemRemoved(it)
                favouriteRvAdapter?.notifyItemRangeChanged(it,list.size)
            }else{
                favouriteRvAdapter?.notifyItemChanged(it)
            }
        }
    }

}
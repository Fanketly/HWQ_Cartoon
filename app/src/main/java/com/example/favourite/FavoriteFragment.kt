package com.example.favourite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.repository.model.FavouriteInfor
import com.example.viewModel.CartoonViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private var favouriteRvAdapter: FavouriteRvAdapter? = null
    private lateinit var viewModel:CartoonViewModel
    //需要传递的数据
    private var name = ""
    private var img = ""
    private var position = 0
    private var mark = R.id.homeFragment
    private lateinit var list:MutableList<FavouriteInfor>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
         list = viewModel.favourite
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.mgs3List.size > 0)
            viewModel.onMsg3Dismiss()
        if (favouriteRvAdapter == null)
            favouriteRvAdapter = FavouriteRvAdapter(list, R.layout.rv_item_favourite, context)
        rvFavourite.setUpWithGrid(favouriteRvAdapter,3)
        rvFavourite.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        favouriteRvAdapter?.setOnClick { position ->
           val favouriteInfor = list[position]
            viewModel.favouriteGet(favouriteInfor.url)
            name = favouriteInfor.title
            img = favouriteInfor.imgUrl
            this.position = position
        }
        //msg3集数
        viewModel.liveDataMsg3.observe(viewLifecycleOwner, { msg3: List<CartoonInfor?> ->
            if (msg3.isNotEmpty()) {
                Log.i("TAG", "msg3: ")
                val bundle = Bundle()
                bundle.putString("name", name)
                bundle.putString("img", img)
                bundle.putInt("position", position)
                bundle.putInt("mark", mark)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favoriteFragment_to_detailedFragment, bundle)
            }
        })
    }
}
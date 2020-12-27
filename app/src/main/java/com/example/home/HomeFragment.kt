package com.example.home

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHomeBinding
import com.example.viewModel.CartoonViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.indicator.CircleIndicator


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    //需要传递的数据
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = viewModel<CartoonViewModel>(CartoonViewModel::class.java)
        if (viewModel.cartoonInfors.size == 0)
            viewModel.getHomeCartoon()
        var cartoonRvAdapter: CartoonRvAdapter? = null
        //轮播图
        if (viewModel.bannerList.size == 0)
            viewModel.getBanner()
//        b.bannerHome.setBannerGalleryMZ(500)
        viewModel.bannerLiveData.observe(viewLifecycleOwner) { list ->
            b.bannerHome.addBannerLifecycleObserver(this).let {
                it.adapter = BannerHomeAdapter(list, requireContext())
                it.indicator = CircleIndicator(context)
            }
        }


        //搜索栏
        b.searchHome.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (!p0?.trim().isNullOrEmpty()) {
                        viewModel.search(p0)
                      isIconified = true
                      clearFocus() // 不获取焦点
                      onActionViewCollapsed()  //可以收起SearchView视图
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }

        //rv
        b.rvHome.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        b.rvHome.addItemDecoration(SpacesItemDecoration(30))
        //加载主页
        viewModel.homeLiveData.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            if (cartoonRvAdapter == null) {
                cartoonRvAdapter =
                    CartoonRvAdapter(viewModel.cartoonInfors, context)
                b.rvHome.setUpWithLinear(cartoonRvAdapter)
                cartoonRvAdapter!!.setOnClick { position ->
                    viewModel.getHomeCartoon(position)
                }
            } else {
                cartoonRvAdapter!!.notifyDataSetChanged()
                b.refreshCartoon.closeHeaderOrFooter()
            }
        })



        b.refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                viewModel.refreshPager()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel.nextPager()
            }

        })

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "HOmeonDestroy: ")
    }
}
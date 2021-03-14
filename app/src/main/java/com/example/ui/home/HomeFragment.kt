package com.example.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.databinding.adapters.ViewBindingAdapter.setOnClick
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.BannerImageAdapter
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.HomeRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.*
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHomeBinding
import com.example.viewModel.CartoonViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.indicator.CircleIndicator


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: CartoonViewModel by activityViewModels()


    //需要传递的数据
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.cartoonInfors.size == 0)
            viewModel.getHomeCartoon()
//        var cartoonRvAdapter: CartoonRvAdapter? = null
        var homeRvAdapter: HomeRvAdapter? = null
        //推荐
        viewModel.get57Recommend()
        viewModel.homeRecommendLiveData.observe(viewLifecycleOwner) {
            val adapter = CartoonRvAdapter(it, requireContext(), R.layout.rv_item_home_recommend)
            adapter.setOnClick { p ->
                viewModel.getHomeRecommendCartoon(p)
            }
            b.rvHomeRecommend.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            b.rvHomeRecommend.setUpWithLinearHORIZONTAL(adapter)
        }
        //轮播图
//        if (viewModel.bannerList.size == 0)
//        viewModel.getBanner()
//        viewModel.bannerLiveData.observe(viewLifecycleOwner) { list ->
        b.bannerHome.apply {
            addBannerLifecycleObserver(this@HomeFragment)
            adapter = BannerImageAdapter(viewModel.bannerList)
            indicator = CircleIndicator(context)
        }
//        }
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
//        b.rvHome.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        b.rvHome.addItemDecoration(SpacesItemDecoration(20))
        //加载主页
        viewModel.homeLiveData.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            if (homeRvAdapter == null) {
                homeRvAdapter = HomeRvAdapter(viewModel.cartoonInfors)
                homeRvAdapter?.apply {
                    with(b.rvHome) {
                        addItemDecoration(SpacesItemDecoration(20))
                        setUpWithGrid(this@apply, 2)
                    }
                    setOnClick { p ->
                        viewModel.getHomeCartoon(p)
                    }
                }
            } else {
                homeRvAdapter?.notifyDataSetChanged()
            }
//            if (cartoonRvAdapter == null) {
//                cartoonRvAdapter =
//                    CartoonRvAdapter(
//                        viewModel.cartoonInfors,
//                        requireContext(),
//                        R.layout.cartoon_rv_item
//                    )
//                b.rvHome.setUpWithGrid(cartoonRvAdapter, 2)
//                cartoonRvAdapter!!.setOnClick { position ->
//                    viewModel.getHomeCartoon(position)
//                }
//            } else {
//                cartoonRvAdapter!!.notifyDataSetChanged()
//            }
            b.refreshCartoon.closeHeaderOrFooter()
        })
        //刷新和加载
        b.refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                viewModel.refreshPager()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel.nextPager()
            }

        })

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            b.bannerHome.stop()
        else
            b.bannerHome.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "HOmeonDestroy: ")
    }
}
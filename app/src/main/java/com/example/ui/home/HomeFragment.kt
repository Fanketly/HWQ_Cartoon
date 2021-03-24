package com.example.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.*
import com.example.base.*
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHomeBinding
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.SearchViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.indicator.CircleIndicator


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: CartoonViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.cartoonInfors.size == 0)
            viewModel.getHomeCartoon()
        var homeRvAdapter: HomeRvAdapter? = null
        //推荐
        viewModel.get57Recommend()
        viewModel.homeRecommendLiveData.observe(viewLifecycleOwner) {
            val adapter = HomeRecommendRvAdapter(it)
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
            val id: Int = this.context.resources
                .getIdentifier("android:id/search_src_text", null, null)
            val tv = findViewById<TextView>(id)
            tv.setTextColor(Color.WHITE)
            tv.setHintTextColor(ContextCompat.getColor(context, R.color.home_background))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    isIconified = true
                    if (!p0?.trim().isNullOrEmpty()) {
                        searchViewModel.search(p0)
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

    override fun viewBinding(container: ViewGroup): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater,container,false)
    }
}
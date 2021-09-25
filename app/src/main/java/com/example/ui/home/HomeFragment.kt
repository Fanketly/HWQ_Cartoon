package com.example.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.*
import com.example.base.*
import com.example.hwq_cartoon.StateEnum
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.databinding.FragmentHomeBinding
import com.example.hwq_cartoon.setUpWithGrid
import com.example.hwq_cartoon.setUpWithLinear
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.SearchViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//dmzj漫画完整地址在RequestUtil的loadCartoon
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: CartoonViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "HomeFragment_onViewCreated ")
        //渐入动画
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(400)
//            for(i in 1 until 4) {
//                delay(100)
//                b.layHome.scaleX = b.layHome.scaleX + 0.1F
//                b.layHome.scaleY = b.layHome.scaleY + 0.1F
//            }
//            viewModel.bottomLiveData.postValue(true)
//            delay(500)
//            for (i in 1 until 27) {
//                delay(50)
//                b.layHome.scaleX = b.layHome.scaleX - 0.05F
//                b.layHome.scaleY = b.layHome.scaleY - 0.05F
//            }
//            Log.i(TAG, "onViewCreated: ${b.layHome.scaleX}")
//            delay(100)
//            viewModel.bottomLiveData.postValue(false)
//            for (i in 1 until 100) {
//                delay(5)
//                b.searchHome.alpha = (i + 10) / 100f
//                b.barHome.alpha = (i + 5) / 100f
//                b.refreshCartoon.alpha = i / 100f
//                viewModel.bottomAlphaLiveData.postValue(i / 100f)
//            }
//        }
        //加载骨架屏
        val homeShimmerRvAdapter = HomeShimmerRvAdapter(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        b.rvHome.setUpWithGrid(homeShimmerRvAdapter, 2)
        b.rvHomeKB.setUpWithGrid(homeShimmerRvAdapter, 2, RecyclerView.HORIZONTAL)
        b.rvHomeRecommend.setUpWithLinear(homeShimmerRvAdapter, RecyclerView.HORIZONTAL)
        var homeRvAdapter: HomeRvAdapter? = null
        var homeRecommendRvAdapter: HomeRecommendRvAdapter? = null
        var homeKBRvAdapter: HomeRecommendRvAdapter? = null
        //优酷
        viewModel.getYouKu()
        viewModel.homeRecommendLiveData.observe(viewLifecycleOwner) {
            Log.i("TAG", "homeRecommendLiveData:$it ")
            if (it == StateEnum.NOTHING) return@observe
            if (homeRecommendRvAdapter == null) {
                homeRecommendRvAdapter = HomeRecommendRvAdapter(viewModel.homeRecommendList)
                homeRecommendRvAdapter?.setOnClick { p ->
                    viewModel.getHomeYouKuCartoon(p)
                }
                b.rvHomeRecommend.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                b.rvHomeRecommend.setUpWithLinear(
                    homeRecommendRvAdapter,
                    RecyclerView.HORIZONTAL
                )
            } else {
                homeRecommendRvAdapter?.notifyDataSetChanged()
            }
        }
        //拷贝
        viewModel.getKaobei()
        viewModel.honeKBLiveData.observe(viewLifecycleOwner) {
            Log.i("TAG", "honeKBLiveData:$it ")
            if (it == StateEnum.NOTHING) return@observe
            if (homeKBRvAdapter == null) {
                homeKBRvAdapter = HomeRecommendRvAdapter(viewModel.homeKBList)
                homeKBRvAdapter!!.setOnClick { p ->
                    viewModel.getHomeKBCartoon(p)
                }
                b.rvHomeKB.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                b.rvHomeKB.setUpWithGrid(homeKBRvAdapter, 2, RecyclerView.HORIZONTAL)
            } else {
                homeKBRvAdapter?.notifyDataSetChanged()
            }
        }
        //加载主页 如果true加载数据 点击导航栏false上拉刷新
        viewModel.getHomeCartoon()
        viewModel.homeLiveData.observe(viewLifecycleOwner, {
            Log.i("TAG", "homeLiveData:$it ")
            when (it!!) {
                StateEnum.SUCCESS -> {
                    if (homeRvAdapter == null) {
                        homeRvAdapter = HomeRvAdapter(viewModel.cartoonInfoList)
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
                        b.refreshCartoon.closeHeaderOrFooter()
                    }
                }
                StateEnum.REFRESH -> {
                    b.rvHome.scrollToPosition(0)
                    b.refreshCartoon.autoRefresh()
                }
                StateEnum.FAIL -> {
                    homeRvAdapter?.notifyDataSetChanged()
                    b.refreshCartoon.closeHeaderOrFooter()
                }
                StateEnum.NOTHING -> {
                }
            }
        })
        //轮播图
        b.bannerHome.apply {
            setPageTransformer(ScaleInTransformer())
            setBannerRound(20f)
            addBannerLifecycleObserver(this@HomeFragment)
            adapter = BannerImageAdapter(viewModel.bannerList)
            indicator = CircleIndicator(context)
        }
        viewModel.lbtLiveData.observe(viewLifecycleOwner) {
            if (it)
                b.bannerHome.stop()
            else
                b.bannerHome.start()
        }

        //共享动画
//        viewModel.detailViewModel.msg3LiveData.observe(viewLifecycleOwner) {
//            val bundle = viewModel.detailViewModel.bundle
//            if (bundle.getInt("mark") != R.id.homeFragment) return@observe
//            val detailedFragment = DetailedFragment()
//            detailedFragment.arguments = bundle
//            with(detailedFragment) {
//                sharedElementEnterTransition = DetailTransition()
//                enterTransition = Fade().setDuration(1000)
//                exitTransition = Fade()
//                sharedElementReturnTransition = DetailTransition()
//
//            }
//            requireActivity().supportFragmentManager.commit {
//                addSharedElement(homeRvAdapter!!.img, getString(R.string.tran1))
//                replace(R.id.layMain2, detailedFragment, "detail")
//            }
//        }


        //搜索栏
        b.searchHome.apply {
            isSubmitButtonEnabled = true
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


        //刷新和加载
        b.refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                viewModel.refreshPager()
                viewModel.getYouKu()
                viewModel.getKaobei()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel.nextPager()
            }

        })

    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyView()
        Log.i(TAG, "HomeOnDestroy: ")
    }

    override fun viewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }
}
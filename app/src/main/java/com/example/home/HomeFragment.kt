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
import com.example.detailed.DetailedFragment
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHomeBinding
import com.example.repository.model.CartoonInfor
import com.example.search.SearchFragment
import com.example.viewModel.CartoonViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    //需要传递的数据
    private var name = ""
    private var img = ""
    private var position = 0
    private var mark = R.id.homeFragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = viewModel<CartoonViewModel>(CartoonViewModel::class.java)
        if (viewModel.cartoonInfors.size == 0)
            viewModel.getHomeCartoon()
        var cartoonRvAdapter: CartoonRvAdapter? = null
        //搜索栏
        b.searchHome.isSubmitButtonEnabled = true
        b.searchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0?.trim().isNullOrEmpty()) {
                    viewModel.search(p0)
                    b.searchHome.isIconified=true
                    b.searchHome.clearFocus() // 不获取焦点
                    viewModel.searchLiveData.observe(viewLifecycleOwner) {
                        Log.i(TAG, "onQueryTextSubmit: $it")
                        if (it) {
                            viewModel.bottomLiveData.value = true
                            beginTransaction(null, SearchFragment::class.java, R.id.layHome)
                            viewModel.searchLiveData.removeObservers(viewLifecycleOwner)
                        } else {
                            shortToast("没找到此漫画")
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

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
                    name = viewModel.cartoonInfors[position].titile
                    img = viewModel.cartoonInfors[position].img
                    this.position = position
                }
            } else {
                cartoonRvAdapter!!.notifyDataSetChanged()
                b.refreshCartoon.closeHeaderOrFooter()
            }
        })

        //msg3集数
        viewModel.msg3LiveData.observe(viewLifecycleOwner, { msg3: List<CartoonInfor?> ->
            if (msg3.isNotEmpty()) {
                Log.i("TAG", "msg3: ")
                val bundle = Bundle()
                bundle.putString("name", name)
                bundle.putString("img", img)
                bundle.putInt("position", position)
                bundle.putInt("mark", mark)
                viewModel.bottomLiveData.value = true
                beginTransaction(bundle, DetailedFragment::class.java, R.id.layHome)
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
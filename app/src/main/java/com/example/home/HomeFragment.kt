package com.example.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(R.layout.fragment_home) {

    //需要传递的数据
    private var name = ""
    private var img = ""
    private var position = 0
    private var mark = R.id.homeFragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        if (viewModel.cartoonInfors.size==0)
        viewModel.getHomeCartoon()
        var cartoonRvAdapter: CartoonRvAdapter? = null
        //rv
        rvHome.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        rvHome.addItemDecoration(SpacesItemDecoration(30))
        //加载主页
        viewModel.liveDataCartoon.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            if (cartoonRvAdapter == null) {
                cartoonRvAdapter =
                    CartoonRvAdapter(viewModel.cartoonInfors, R.layout.cartoon_rv_item, context)
                rvHome.setUpWithLinear(cartoonRvAdapter)
                cartoonRvAdapter!!.setOnClick { position ->
                    viewModel.getHomeCartoon(position)
                    name = viewModel.cartoonInfors[position].titile
                    img = viewModel.cartoonInfors[position].img
                    this.position = position
                }
            } else {
                cartoonRvAdapter!!.notifyDataSetChanged()
                refreshCartoon.closeHeaderOrFooter()
            }
        })

        //msg3集数
        viewModel.liveDataMsg3.observe(viewLifecycleOwner, { msg3: List<CartoonInfor?> ->
            if (msg3.isNotEmpty()) {
                Log.i("TAG", "msg3: ")
                val bundle = Bundle()
                bundle.putString("name", name)
                bundle.putString("img", img)
                bundle.putInt("position", position)
                bundle.putInt("mark", mark)
                viewModel.bottomLiveData.value = true
                beginTransaction(bundle, R.id.layHome)
            }
        })

        refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                viewModel.refreshPager()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel.nextPager()
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onHomeDestroyView: ")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "HOmeonDestroy: ")
    }
}
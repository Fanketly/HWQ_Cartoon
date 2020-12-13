package com.example.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        viewModel.getHomeCartoon()
        cartoonRvAdapter =
            CartoonRvAdapter(viewModel.cartoonInfors, R.layout.cartoon_rv_item, context)
        cartoonRvAdapter?.setOnClick { position ->
            viewModel.getHomeCartoon(position)
            name = viewModel.cartoonInfors[position].titile
            img = viewModel.cartoonInfors[position].img
            this.position = position
        }
    }

    private var cartoonRvAdapter: CartoonRvAdapter? = null
    private lateinit var viewModel: CartoonViewModel

    //需要传递的数据
    private var name = ""
    private var img = ""
    private var position = 0
    private var mark = R.id.homeFragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //返回时清除msg3
        if (viewModel.mgs3List.size > 0)
            viewModel.onMsg3Dismiss()
        //rv
        rvHome.adapter = cartoonRvAdapter
        rvHome.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        rvHome.addItemDecoration(SpacesItemDecoration(15))
        rvHome.layoutManager = LinearLayoutManager(context)
        //加载主页
        viewModel.liveDataCartoon.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            cartoonRvAdapter?.notifyDataSetChanged()
            refreshCartoon.closeHeaderOrFooter()
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
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailedFragment, bundle)
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
        Log.i("TAG", "onDestroyView: ")
    }

}
package com.example.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        onTransformationStartContainer()
        Log.i(TAG, "onCreate: ")
        viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]
        viewModel.getHomeCartoon()
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
        if (viewModel.cartoonInforList.size > 0)
            viewModel.onMsg3Dismiss()
        viewModel.liveDataCartoon.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            if (cartoonRvAdapter == null) {
                cartoonRvAdapter = CartoonRvAdapter(it, R.layout.cartoon_rv_item, context)
                Log.i("TAG", "adapter: ")
            }
            rvHome.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            rvHome.setUpWithLinear(cartoonRvAdapter)
            rvHome.addItemDecoration(SpacesItemDecoration(15))
            cartoonRvAdapter?.setOnClick { position ->
                viewModel.getHomeCartoon(position)
                name = it[position].titile
                img = it[position].img
                this.position = position
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
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailedFragment, bundle)
            }


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("TAG", "onDestroyView: ")
    }

}
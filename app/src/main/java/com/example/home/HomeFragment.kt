package com.example.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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
private lateinit var viewModel: CartoonViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.cartoonInforList.size>0)
        viewModel.onMsg3Dismiss()
        viewModel.liveDataCartoon.observe(viewLifecycleOwner, {
            Log.i("TAG", "o: ")
            val cartoonRvAdapter =
                CartoonRvAdapter(it, R.layout.cartoon_rv_item, context)
            rvHome.setUpWithLinear(cartoonRvAdapter)
            rvHome.addItemDecoration(SpacesItemDecoration(15))
            cartoonRvAdapter.setOnClick(object : CartoonRvAdapter.OnClick {
                override fun onClick(position: Int) {
                    viewModel.getHomeCartoon(position)
                }

                override fun longOnClick(position: Int) {
//                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
//                    viewModel.setFavouriteCartoon(position)
                }
            })
        })

        //msg3集数
        viewModel.liveDataMsg3.observe(viewLifecycleOwner, { msg3: List<CartoonInfor?> ->
            if (msg3.isNotEmpty()) {
                Log.i("TAG", "msg3: ")
                    Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailedFragment)
            }


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("TAG", "onDestroyView: ")
    }

}
package com.example.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel
import com.skydoves.transformationlayout.onTransformationStartContainer
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onTransformationStartContainer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[CartoonViewModel::class.java]

        viewModel.getHomeCartoon()
        viewModel.liveDataCartoon.observe(viewLifecycleOwner, {
            Log.i("TAG", "onActivityCreated: ")
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
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailedFragment)
            }
//                val builder = AlertDialog.Builder(context)
//                val alertDialog = builder.create()
//                val view3 =
//                    LayoutInflater.from(context).inflate(R.layout.dialog_cartoon, null, false)
//                val cartoonRvAdapter1 =
//                    CartoonDialogRvAdapter(context, R.layout.cartoon_dialog_rv_item, msg3)
//                val rv3 = view3.findViewById<RecyclerView>(R.id.rvCartoon)
//                rv3.setUpWithGrid(cartoonRvAdapter1, 4)
//                alertDialog.setOnDismissListener { viewModel.onMsg3Dismiss() }
//                alertDialog.setView(view3)
//                alertDialog.show()
//                //点击集数
//                cartoonRvAdapter1.setOnClick { position: Int -> viewModel.msg3Send(position) }


        })
    }

}
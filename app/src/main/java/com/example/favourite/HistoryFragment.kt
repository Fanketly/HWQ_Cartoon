package com.example.favourite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHistoryBinding
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    private lateinit var viewModel: FavouriteViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val cartoonViewModel: CartoonViewModel = viewModel(CartoonViewModel::class.java)
        val adapter = HistoryRvAdapter(requireContext(), viewModel.historyList)
        b.rvHistory.addItemDecoration(SpacesItemDecoration(30))
        b.rvHistory.setUpWithLinear(adapter)
        viewModel.historyLivaData.observe(viewLifecycleOwner) {
            if (it == -2) return@observe
            if (it == -1) {
                adapter.notifyDataSetChanged()
                return@observe
            }
            adapter.notifyItemChanged(it)
        }
        viewModel.historyLivaData.value=-2//只要没发送新的value 就return
        adapter.onclick {
            cartoonViewModel.historyGet(viewModel.historyList[it])
        }
    }


}
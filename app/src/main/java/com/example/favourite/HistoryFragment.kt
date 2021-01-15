package com.example.favourite

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentHistoryBinding
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val cartoonViewModel: CartoonViewModel = viewModel(CartoonViewModel::class.java)
        val adapter = HistoryRvAdapter(requireContext(), viewModel.historyList)
        b.rvHistory.addItemDecoration(SpacesItemDecoration(30))
        b.rvHistory.setUpWithLinear(adapter)
        viewModel.historyLivaData.observe(viewLifecycleOwner) {
            adapter.notifyItemChanged(it)
        }
        adapter.onclick {
            cartoonViewModel.historyGet(viewModel.historyList[it])
            viewModel.tabLayLiveData.value = true
        }
    }
}
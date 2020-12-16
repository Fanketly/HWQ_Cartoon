package com.example.favourite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.viewModel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        val adapter = HistoryRvAdapter(requireContext(), viewModel.historyList)
        rvHistory.addItemDecoration(SpacesItemDecoration(30))
        rvHistory.setUpWithLinear(adapter)
        viewModel.historyLivaData.observe(viewLifecycleOwner){
            Log.i("TAG", "TAGG: $it")
            adapter.notifyItemChanged(it)
        }
    }
}
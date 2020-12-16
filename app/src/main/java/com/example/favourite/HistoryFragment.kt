package com.example.favourite

import android.os.Bundle
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
        val viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        val adapter = HistoryRvAdapter(requireContext(), viewModel.getAll())
        rvHistory.addItemDecoration(SpacesItemDecoration(30))
        rvHistory.setUpWithLinear(adapter)
    }
}
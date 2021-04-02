package com.example.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.adapter.HistoryRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.databinding.FragmentHistoryBinding
import com.example.viewModel.FavouriteViewModel

class HistoryFragment : Fragment() {
    private val viewModel: FavouriteViewModel by activityViewModels()
    private lateinit var b: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HistoryRvAdapter(viewModel.historyList)
        with(b.rvHistory) {
            addItemDecoration(SpacesItemDecoration(30))
            setUpWithLinear(adapter)
        }
        viewModel.historyLivaData.observe(viewLifecycleOwner) {
            if (it == -2) return@observe
            if (it == -1) {
                adapter.notifyDataSetChanged()
                return@observe
            }
            adapter.notifyItemChanged(it)
        }
        viewModel.historyLivaData.value = -2//只要没发送新的value 就return
        adapter.setOnClick {
            viewModel.historyGet(it)
        }
    }


}
package com.example.ui.classification

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.adapter.FavouriteRvAdapter
import com.example.base.BaseFragment
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.setUpWithGrid
import com.example.hwq_cartoon.setUpWithLinear
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.repository.model.SpeciesInfo
import com.example.viewModel.SpeciesViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>() {
    private var adapterTop: DataBindingAdapter<SpeciesInfo>? = null
    private val viewModel: SpeciesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //标记分类
        var mark = 0
        var pp = 0
        viewModel.getSpeciesType()
        var adapter: FavouriteRvAdapter? = null
        b.rvSpeciesTop.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        b.refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                if (adapter == null)
                    viewModel.getSpeciesType()
                else
                    viewModel.refresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel.loadMoreData()
            }

        })
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                Log.i(TAG, "init species adapter ")
                adapterTop = DataBindingAdapter(
                    viewModel.typesList,
                    BR.type,
                    R.layout.rv_item_species_top
                )
                adapterTop?.apply {
                    setOnClick(R.id.tvSpeciesTopRvItem) { p, t ->
                        if (viewModel.switchCategory(t.id))
                            pp = p
                    }
                    getView { p, _, v ->
                        val tv = v.findViewById<TextView>(R.id.tvSpeciesTopRvItem)
                        if (p == mark)
                            tv.setTextColor(requireActivity().getColor(R.color.theme_blue))
                        else
                            tv.setTextColor(requireActivity().getColor(R.color.hui))
                    }
                    b.rvSpeciesTop.setUpWithLinear(this)
                }
                adapter = FavouriteRvAdapter(it)
                adapter?.run {
                    b.rvSpecies.setUpWithGrid(this, 3)
                    setOnClick(onclick = { p -> viewModel.getSpeciesCartoon(p) })
                }
                b.refreshCartoon.closeHeaderOrFooter()
            } else {
                b.refreshCartoon.closeHeaderOrFooter()
                adapter?.notifyDataSetChanged()
            }
        }
        viewModel.adapterTopLiveData.observe(viewLifecycleOwner) {
            Log.i(TAG, "refresh or switch")
            adapterTop?.notifyItemChanged(mark)
            mark = pp
            adapterTop?.notifyItemChanged(mark)
        }
    }

    override fun viewBinding(container: ViewGroup?): FragmentSpeciesBinding {
        return FragmentSpeciesBinding.inflate(layoutInflater, container, false)
    }

}
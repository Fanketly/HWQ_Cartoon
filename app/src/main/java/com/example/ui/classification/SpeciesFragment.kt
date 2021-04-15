package com.example.ui.classification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.adapter.FavouriteRvAdapter
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.repository.model.SpeciesInfo
import com.example.viewModel.SpeciesViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>() {
    private var adapterTop: DataBindingAdapter<SpeciesInfo>? = null
    private val viewModel: SpeciesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //标记分类
        var mark = 0
        var pp = 0
        //
        var species = "0"
        var pager = 1
        viewModel.getSpeciesType()
        var adapter: FavouriteRvAdapter? = null
        b.rvSpeciesTop.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        b.refreshCartoon.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                viewModel.getSpeciesData(species, 1)
                viewModel.getSpeciesType()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pager += 1
                viewModel.loadMoreData(species, pager)
            }

        })
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                adapterTop = DataBindingAdapter(
                    viewModel.typeList,
                    BR.type,
                    R.layout.rv_item_species_top
                )
                adapterTop?.apply {
                    setOnClick(R.id.tvSpeciesTopRvItem) { p, t ->
                        species = t.id
                        viewModel.getSpeciesData(species, 1)
                        pp = p
                    }
                    getView { p, _, v ->
                        val tv = v.findViewById<TextView>(R.id.tvSpeciesTopRvItem)
                        if (p == mark)
                            tv.setTextColor(requireActivity().getColor(R.color.theme_blue))
                        else
                            tv.setTextColor(requireActivity().getColor(R.color.species_hui))
                    }
                    b.rvSpeciesTop.setUpWithLinear(this)
                }
                adapter = FavouriteRvAdapter(viewModel.speciesList)
                b.rvSpecies.setUpWithGrid(adapter, 3)
                adapter?.setOnClick(onclick = { viewModel.getSpeciesCartoon(it) })
            } else {
                b.refreshCartoon.closeHeaderOrFooter()
                adapter?.notifyDataSetChanged()
            }
        }
        viewModel.adapterTopLiveData.observe(viewLifecycleOwner) {
            adapterTop?.notifyItemChanged(mark)
            mark = pp
            adapterTop?.notifyItemChanged(mark)
        }
    }

    override fun viewBinding(container: ViewGroup): FragmentSpeciesBinding {
        return FragmentSpeciesBinding.inflate(layoutInflater, container, false)
    }

}
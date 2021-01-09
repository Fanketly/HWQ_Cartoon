package com.example.classification

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.base.BaseFragment
import com.example.base.logi
import com.example.base.setUpWithGrid
import com.example.base.setUpWithLinear
import com.example.favourite.FavouriteRvAdapter
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.viewModel.CartoonViewModel


class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>(R.layout.fragment_species) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logi("onSpeciesActivityCreated: ")
        var mark = 0
        val viewModel = viewModel<CartoonViewModel>(CartoonViewModel::class.java)
        viewModel.getSpeciesType()
        var adapter: FavouriteRvAdapter? = null
        b.rvSpeciesTop.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                val adapterTop = DataBindingAdapter(
                    viewModel.typeList,
                    BR.type,
                    R.layout.rv_item_species_top
                )
                adapterTop.setOnClick(R.id.tvSpeciesTopRvItem) { p, t ->
                    adapterTop.notifyItemChanged(mark)
                    mark = p
                    adapterTop.notifyItemChanged(mark)
                    viewModel.species = t.id
                    viewModel.getSpeciesData()
                }
                adapterTop.getView { p, _, v ->
                    val tv = v.findViewById<TextView>(R.id.tvSpeciesTopRvItem)
                    if (p == mark)
                        tv.setTextColor(requireActivity().getColor(R.color.theme_blue))
                    else
                        tv.setTextColor(requireActivity().getColor(R.color.species_hui))
                }
                adapter = FavouriteRvAdapter(viewModel.speciesList, requireContext())
//                b.rvSpeciesTop.setUpWithGrid(adapterTop, 6)
                b.rvSpeciesTop.setUpWithLinear(adapterTop)
                b.rvSpecies.setUpWithGrid(adapter, 3)
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logi("SpeciesonDestroy: ")
    }
}
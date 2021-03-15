package com.example.ui.classification

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.base.BaseFragment
import com.example.base.setUpWithGrid
import com.example.base.setUpWithLinear
import com.example.adapter.FavouriteRvAdapter
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.repository.model.SpeciesInfor
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.SpeciesViewModel


class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>(R.layout.fragment_species) {
    private var adapterTop: DataBindingAdapter<SpeciesInfor>? = null
    private val viewModel: CartoonViewModel by activityViewModels()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var mark = 0
        var pp = 0
        viewModel.getSpeciesType()
        var adapter: FavouriteRvAdapter? = null
        b.rvSpeciesTop.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                adapterTop = DataBindingAdapter(
                    viewModel.typeList,
                    SpeciesViewModel::class.java,
                    SpeciesInfor::class.java,
                    BR.type,
                    R.layout.rv_item_species_top
                )
                adapterTop?.setOnClick(R.id.tvSpeciesTopRvItem) { p, t ->
                    viewModel.species = t.id
                    viewModel.getSpeciesData()
                    pp = p
                }
                adapterTop?.getView { p, _, v ->
                    val tv = v.findViewById<TextView>(R.id.tvSpeciesTopRvItem)
                    if (p == mark)
                        tv.setTextColor(requireActivity().getColor(R.color.theme_blue))
                    else
                        tv.setTextColor(requireActivity().getColor(R.color.species_hui))
                }
                adapter = FavouriteRvAdapter(viewModel.speciesList)
                b.rvSpeciesTop.setUpWithLinear(adapterTop)
                b.rvSpecies.setUpWithGrid(adapter, 3)
                adapter?.setOnClick(onclick = { viewModel.getSpeciesCartoon(it) }) {}
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
        viewModel.adapterTopLiveData.observe(viewLifecycleOwner) {
            adapterTop?.notifyItemChanged(mark)
            mark = pp
            adapterTop?.notifyItemChanged(mark)
        }
    }

}
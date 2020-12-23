package com.example.classification

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.base.BaseFragment
import com.example.base.TAG
import com.example.base.setUpWithGrid
import com.example.favourite.FavouriteRvAdapter
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.repository.model.SpeciesInfor
import com.example.viewModel.CartoonViewModel


class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>(R.layout.fragment_species) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onSpeciesActivityCreated: ")
        val viewModel = viewModel<CartoonViewModel>(CartoonViewModel::class.java)
        viewModel.getSpeciesType()
        viewModel.getSpeciesData()
        var adapter: FavouriteRvAdapter? = null
        b.rvSpeciesTop.overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                val adapterTop = DataBindingAdapter<SpeciesInfor>(
                    viewModel.typeList,
                    BR.type,
                    R.layout.rv_item_species_top
                )
                adapterTop.setOnClick(R.id.tvSpeciesTopRvItem) { _, t ->
                    viewModel.species=t.id
                    viewModel.getSpeciesData()
                }
                adapter = FavouriteRvAdapter(viewModel.speciesList, requireContext())
                b.rvSpeciesTop.setUpWithGrid(adapterTop, 7)
                b.rvSpecies.setUpWithGrid(adapter, 4)
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "SpeciesonDestroy: ")
    }
}
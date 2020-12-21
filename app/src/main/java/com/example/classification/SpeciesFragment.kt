package com.example.classification

import android.os.Bundle
import com.example.base.BaseFragment
import com.example.favourite.FavouriteRvAdapter
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSpeciesBinding
import com.example.viewModel.CartoonViewModel


class SpeciesFragment : BaseFragment<FragmentSpeciesBinding>(R.layout.fragment_species) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = viewModel<CartoonViewModel>(CartoonViewModel::class.java)
        viewModel.getSpecies()
        var adapter: FavouriteRvAdapter? = null
        viewModel.speciesLiveData.observe(viewLifecycleOwner) {
            if (adapter == null) {
                adapter = FavouriteRvAdapter(viewModel.speciesList,requireContext())
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
    }
}
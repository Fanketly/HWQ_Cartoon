package com.example.search

import android.os.Bundle
import android.widget.SearchView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSearchBinding
import com.example.viewModel.CartoonViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private lateinit var viewModel: CartoonViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapter: CartoonRvAdapter? = null
        viewModel = viewModel(CartoonViewModel::class.java)
        b.btnSearchBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
        b.searchSearch.isSubmitButtonEnabled = true
        b.searchSearch.isIconifiedByDefault = false
        b.rvSearch.addItemDecoration(SpacesItemDecoration(30))
        adapter?.setOnClick {
            viewModel.getSearch(it)
        }
        viewModel.msg3LiveData.observe(viewLifecycleOwner){

        }
        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            if (it) {
                if (adapter == null) {
                    adapter = CartoonRvAdapter(viewModel.searchList, context)
                    b.rvSearch.setUpWithLinear(adapter)
                } else {
                    adapter?.notifyDataSetChanged()
                }
            } else {
                shortToast("没找到此漫画")
            }
        }
        b.searchSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.clearSearchList()
                viewModel.search(p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSearchList()
        viewModel.bottomLiveData.value = false
    }
}
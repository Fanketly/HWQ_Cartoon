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
    private var name = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapter: CartoonRvAdapter? = null
        viewModel = viewModel(CartoonViewModel::class.java)
        b.btnSearchBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
        //rv
        b.rvSearch.addItemDecoration(SpacesItemDecoration(30))
        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            if (it) {
                if (adapter == null) {
                    adapter = CartoonRvAdapter(viewModel.searchList, context)
                    b.rvSearch.setUpWithLinear(adapter)
                    adapter?.setOnClick { p ->
                        viewModel.getSearch(p)
                    }
                } else {
                    adapter?.notifyDataSetChanged()
                }
            } else {
                shortToast("没找到此漫画")
            }
        }
        //search
        b.searchSearch.isSubmitButtonEnabled = true
        b.searchSearch.isIconifiedByDefault = false
        b.searchSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (name != p0) {
                    name = p0!!
                    viewModel.clearSearchList()
                    viewModel.search(p0)
                }
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
        viewModel.isSearchFragment = false
    }
}
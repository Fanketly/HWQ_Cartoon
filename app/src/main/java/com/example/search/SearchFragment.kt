package com.example.search

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CartoonRvAdapter
import com.example.adapter.DataBindingAdapter
import com.example.adapter.SearchVpAdapter
import com.example.adapter.SpacesItemDecoration
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSearchBinding
import com.example.repository.model.CartoonInfor
import com.example.viewModel.CartoonViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private lateinit var viewModel: CartoonViewModel
    private var name = ""
    private lateinit var rv: RecyclerView
    private lateinit var rv2: RecyclerView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        b.laySearch.setOnClickListener { }
        var adapter: CartoonRvAdapter? = null
        var adapter2: DataBindingAdapter<CartoonInfor>? = null

        viewModel = viewModel(CartoonViewModel::class.java)
        b.btnSearchBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            if (viewModel.pgLiveData.value == false)
                viewModel.pgLiveData.value = true
            if (b.vpSearch.adapter == null) {
                rv = RecyclerView(requireContext())
                rv2 = RecyclerView(requireContext())
                rv.addItemDecoration(SpacesItemDecoration(30))
                rv2.addItemDecoration(SpacesItemDecoration(30))
                b.vpSearch.adapter = SearchVpAdapter(listOf(rv, rv2))
                b.tabSearch.setupWithViewPager(b.vpSearch)
                b.tabSearch.getTabAt(0)?.text = "动漫之家"
                b.tabSearch.getTabAt(1)?.text = "57漫画"
            }
            when (it) {
                1 -> if (adapter == null) {//动漫之家
                    adapter = CartoonRvAdapter(viewModel.searchList, context)
                    rv.setUpWithLinear(adapter)
                    adapter?.setOnClick { p ->
                        viewModel.getSearch(p)
                    }
                } else {
                    adapter?.notifyDataSetChanged()
                }
                2 -> if (adapter2 == null) {//57漫画
                    adapter2 = DataBindingAdapter(
                        viewModel.searchList57,
                        BR.data,
                        R.layout.rv_item_57_cartoon
                    )
                    rv2.setUpWithLinear(adapter2)
                    adapter2?.setOnClick(R.id.layoutCartoon) { _, t ->
                        viewModel.getSearch57(t)
                    }
                } else {
                    adapter2?.notifyDataSetChanged()
                }
            }
        }
        //search
        b.searchSearch.apply {
            isSubmitButtonEnabled = true
            isIconifiedByDefault = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSearchList()
        viewModel.bottomLiveData.value = false
        viewModel.isSearchFragment = false
    }
}
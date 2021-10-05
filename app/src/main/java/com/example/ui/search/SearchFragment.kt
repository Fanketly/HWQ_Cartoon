package com.example.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.adapter.HomeRvAdapter
import com.example.adapter.SearchVpAdapter
import com.example.base.BaseFragment
import com.example.hwq_cartoon.setUpWithLinear
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.TAG
import com.example.hwq_cartoon.databinding.FragmentSearchBinding
import com.example.repository.model.CartoonInfo
import com.example.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by activityViewModels()
    private var name = ""
    private lateinit var rv: RecyclerView
    private lateinit var rv2: RecyclerView
    private lateinit var rv3: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.laySearch.setOnClickListener { }
        var homeRvAdapter: HomeRvAdapter? = null
        var adapter2: DataBindingAdapter<CartoonInfo>? = null
        var adapter3: DataBindingAdapter<CartoonInfo>? = null
        b.btnSearchBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(this).commit()
        }
//        if (b.vpSearch.adapter == null) {
        rv = RecyclerView(requireContext())
        rv2 = RecyclerView(requireContext())
        rv3 = RecyclerView(requireContext())
        b.vpSearch.adapter = SearchVpAdapter(listOf(rv, rv2, rv3))
        b.vpSearch.offscreenPageLimit = 2
        b.tabSearch.setupWithViewPager(b.vpSearch)
        b.tabSearch.getTabAt(0)?.text = "某漫之家"
        b.tabSearch.getTabAt(1)?.text = "某酷漫画"
        b.tabSearch.getTabAt(2)?.text = "某贝漫画"
//        }
        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            Log.i(TAG, "search: $it")
            if (viewModel.pgLiveData.value == false)
                viewModel.pgLiveData.value = true

            when (it) {
                1 -> if (homeRvAdapter == null) {
                    homeRvAdapter = HomeRvAdapter(viewModel.searchDMZJList)
                    homeRvAdapter?.apply {
                        rv.setUpWithLinear(this)
                        setOnClick { p ->
                            viewModel.getSearch(p)
                        }
                    }
                } else {
                    homeRvAdapter?.notifyDataSetChanged()
                }
                2 -> if (adapter2 == null) {//优酷漫画
                    adapter2 = DataBindingAdapter(
                        viewModel.searchYKList,
                        BR.data,
                        R.layout.rv_item_search
                    )
                    adapter2?.run {
                        rv2.setUpWithLinear(this)
                        setOnClick(R.id.layoutCartoon) { _, t ->
                            viewModel.getSearch(t)
                        }
                    }
                    Log.i(TAG, "search2:adapter ")
                } else {
                    adapter2?.notifyDataSetChanged()
                }
                3 -> if (adapter3 == null) {//拷贝漫画
                    adapter3 = DataBindingAdapter(
                        viewModel.searchKBList,
                        BR.data,
                        R.layout.rv_item_search
                    )
                    adapter3?.run {
                        rv3.setUpWithLinear(this)
                        setOnClick(R.id.layoutCartoon) { _, t ->
                            viewModel.getSearch(t)
                        }
                    }
                    Log.i(TAG, "adapter3:adapter ")
                } else {
                    adapter3?.notifyDataSetChanged()
                }
            }
        }
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //search
        b.searchSearch.apply {
//            val id: Int = context.resources
//                .getIdentifier("android:id/search_src_text", null, null)
//            val tv = findViewById<TextView>(id)
//            tv.setTextColor(Color.WHITE)
            isSubmitButtonEnabled = true
//            isIconifiedByDefault = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    inputMethodManager.hideSoftInputFromWindow(
                        windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
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

    override fun viewBinding(container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater, container, false)
    }
}
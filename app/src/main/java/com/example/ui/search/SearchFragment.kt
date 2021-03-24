package com.example.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.DataBindingAdapter
import com.example.adapter.HomeRvAdapter
import com.example.adapter.SearchVpAdapter
import com.example.base.BaseFragment
import com.example.base.setUpWithLinear
import com.example.hwq_cartoon.BR
import com.example.hwq_cartoon.R
import com.example.hwq_cartoon.databinding.FragmentSearchBinding
import com.example.repository.model.CartoonInfor
import com.example.viewModel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by activityViewModels()
    private var name = ""
    private lateinit var rv: RecyclerView
    private lateinit var rv2: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.laySearch.setOnClickListener { }
        var homeRvAdapter: HomeRvAdapter? = null
        var adapter2: DataBindingAdapter<CartoonInfor>? = null
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
                b.vpSearch.adapter = SearchVpAdapter(listOf(rv, rv2))
                b.tabSearch.setupWithViewPager(b.vpSearch)
                b.tabSearch.getTabAt(0)?.text = "动漫之家"
                b.tabSearch.getTabAt(1)?.text = "57漫画"
            }
            when (it) {
                1 -> if (homeRvAdapter == null) {
                    homeRvAdapter = HomeRvAdapter(viewModel.searchList)
                    homeRvAdapter?.apply {
                        rv.setUpWithLinear(homeRvAdapter)
                        setOnClick { p ->
                            viewModel.getSearch(p)
                        }
                    }
                } else {
                    homeRvAdapter?.notifyDataSetChanged()
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
            val id: Int = this.context.resources
                .getIdentifier("android:id/search_src_text", null, null)
            val tv = findViewById<TextView>(id)
            tv.setTextColor(Color.WHITE)
            isSubmitButtonEnabled = true
//            isIconifiedByDefault = false
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

    override fun viewBinding(container: ViewGroup): FragmentSearchBinding {
       return FragmentSearchBinding.inflate(layoutInflater,container,false)
    }
}
package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.adapter.MainVpAdapter
import com.example.hwq_cartoon.App.Companion.blackTheme
import com.example.hwq_cartoon.databinding.ActivityMainBinding
import com.example.ui.classification.SpeciesFragment
import com.example.ui.detailed.DetailedFragment
import com.example.ui.favourite.FavouriteVpFragment
import com.example.ui.home.HomeFragment
import com.example.ui.me.MeFragment
import com.example.ui.search.SearchFragment
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.FavouriteViewModel
import com.example.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //viewModel
    private val viewModel: CartoonViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()

    //fragment
    private lateinit var fragmentManager: FragmentManager
    private var mark = 0
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        if (blackTheme) {
            Log.i("TAG", "THEME:BLACK ")
            setTheme(R.style.Theme_HWQ_Cartoon_Black)
        } else {
            Log.i("TAG", "THEME:LIGHT ")
            setTheme(R.style.Theme_HWQ_Cartoon)
        }
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
//        //切换界面
        fragmentManager = supportFragmentManager
        val menu = b.bottomNav.menu
        menuItem = menu.getItem(0)
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (mark == 0) {
                        viewModel.homeLiveData.postValue(StateEnum.REFRESH)
                    } else {
                        b.vpMain.setCurrentItem(0, false)
                    }
                }
                R.id.speciesFragment -> b.vpMain.setCurrentItem(1, false)
                R.id.favoriteVpFragment -> b.vpMain.setCurrentItem(2, false)
                R.id.meFragment -> b.vpMain.setCurrentItem(3, false)
            }
            return@setOnNavigationItemSelectedListener true
        }
//        vp
        val mainVpAdapter = MainVpAdapter(
            fragmentManager,
            lifecycle,
            listOf(HomeFragment(), SpeciesFragment(), FavouriteVpFragment(), MeFragment())
        )
        b.vpMain.offscreenPageLimit = 3
        b.vpMain.adapter = mainVpAdapter

        try {
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(b.vpMain)
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            touchSlopField.set(
                recyclerView,
                touchSlopField.get(recyclerView) as Int * 4
            )
        } catch (ignore: Exception) {
        }
        b.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mark = position
                menuItem.isChecked = false
                menuItem = menu[position]
                menuItem.isChecked = true
                when (position) {
                    0 -> {
                        viewModel.lbtLiveData.postValue(false)
                    }
                    1 -> {
                        viewModel.lbtLiveData.postValue(true)
                    }
                    2 -> {
                        favouriteViewModel.likesIsZero()
                        viewModel.lbtLiveData.postValue(true)
                    }
                    3 -> {
                        favouriteViewModel.getSize()
                        viewModel.lbtLiveData.postValue(true)
                    }
                }
            }
        })

        //pg监听
        viewModel.pgLiveData.observe(this) {
            Log.i("TAG", "MainActivity_onCreate:$it ")
            b.pgMain.visibility = if (it) View.GONE else View.VISIBLE
        }
        //底部监听
        viewModel.bottomLiveData.observe(this) {
            b.bottomNav.visibility = if (it) View.GONE else View.VISIBLE
        }
        //集数Detail监听
        if (viewModel.msg3LiveData.value != null) {
            Log.i("TAG", "msg3_value: ")
            viewModel.msg3LiveData.value = null
        }
        viewModel.msg3LiveData.observe(this) {
            if (it != null) {
                b.bottomNav.visibility = View.GONE
                addWithBundle(it, DetailedFragment::class.java)
            }
        }
        //Search监听
        if (searchViewModel.searchLiveData.value != null) {
            Log.i("TAG", "msg3_value: ")
            searchViewModel.searchLiveData.value = null
        }
        searchViewModel.searchLiveData.observe(this) {
            if (it == null) return@observe
            if (!searchViewModel.isSearchFragment) {
                b.bottomNav.visibility = View.GONE
                addWithBundle(null, SearchFragment::class.java)
                searchViewModel.isSearchFragment = true
            }
        }
        if (viewModel.errorLiveData.value != null) {
            Log.i("TAG", "msg3_value: ")
            viewModel.errorLiveData.value = null
        }
        //错误监听
        viewModel.errorLiveData.observe(this) {
            if (it == null) return@observe
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addWithBundle(bundle: Bundle?, clazz: Class<out Fragment>) =
        fragmentManager.commit {
            setCustomAnimations(R.anim.right_in, R.anim.left_out)
            add(R.id.layMain, clazz, bundle, "detail")
//            addToBackStack(null)
        }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "MainActivity_onDestroy: ")
    }

    override fun onBackPressed() {
        val detailedFragment = fragmentManager.findFragmentByTag("detail")
        if (detailedFragment != null) {
            fragmentManager.commit {
                setCustomAnimations(0, R.anim.right_out)
                remove(detailedFragment)
            }
            Log.i("TAG", "onBackPressed: ")
        } else {
            super.onBackPressed()
        }
    }
}
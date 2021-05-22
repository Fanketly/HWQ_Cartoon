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

    //    private val homeFragment: HomeFragment by lazy { HomeFragment() }
//    private val favouriteVpFragment: FavouriteVpFragment by lazy { FavouriteVpFragment() }
//    private val speciesFragment: SpeciesFragment by lazy { SpeciesFragment() }
//    private val meFragment by lazy { MeFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (blackTheme) {
            Log.i("TAG", "MainActivity_onCreate: ")
            setTheme(R.style.Theme_HWQ_Cartoon_Black)
        } else {
            setTheme(R.style.Theme_HWQ_Cartoon)
        }
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

//        //切换界面
        fragmentManager = supportFragmentManager
//        fragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
//            .add(R.id.layMain2, homeFragment).commit()
//        lastFragment = homeFragment
        val menu = b.bottomNav.menu
        menuItem = menu.getItem(0)
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (mark == 0) {
                        viewModel.homeLiveData.postValue(false)
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
            if (it==null)return@observe
            if (!searchViewModel.isSearchFragment)
                when (it) {
                    1 -> {
                        b.bottomNav.visibility = View.GONE
                        addWithBundle(null, SearchFragment::class.java)
                        searchViewModel.isSearchFragment = true
                    }
                }
        }
        //错误监听
        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addWithBundle(bundle: Bundle?, clazz: Class<out Fragment>) =
        fragmentManager.commit {
            setCustomAnimations(R.anim.right_in, R.anim.left_out)
            add(R.id.layMain, clazz, bundle, "detail")
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
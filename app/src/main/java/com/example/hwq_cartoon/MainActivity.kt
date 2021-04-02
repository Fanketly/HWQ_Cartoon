package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.hwq_cartoon.databinding.ActivityMainBinding
import com.example.ui.classification.SpeciesFragment
import com.example.ui.detailed.DetailedFragment
import com.example.ui.favourite.FavouriteVpFragment
import com.example.ui.home.HomeFragment
import com.example.ui.me.MeFragment
import com.example.ui.search.SearchFragment
import com.example.viewModel.CartoonViewModel
import com.example.viewModel.SearchViewModel


class MainActivity : AppCompatActivity() {
    //viewModel
    private val viewModel: CartoonViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    //fragment
    private lateinit var fragmentManager: FragmentManager
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val favouriteVpFragment: FavouriteVpFragment by lazy { FavouriteVpFragment() }
    private val speciesFragment: SpeciesFragment by lazy { SpeciesFragment() }
    private lateinit var lastFragment: Fragment
    private val meFragment by lazy { MeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        //切换界面
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .add(R.id.layMain2, homeFragment).commit()
        lastFragment = homeFragment
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (lastFragment == homeFragment) {
                        viewModel.homeLiveData.postValue(false)
                    } else
                        add(homeFragment)
                }
                R.id.favoriteVpFragment ->
                    add(favouriteVpFragment)
                R.id.speciesFragment ->
                    add(speciesFragment)
                R.id.meFragment -> add(meFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
        //pg监听
        viewModel.pgLiveData.observe(this) {
            b.pgMain.visibility = if (it) View.GONE else View.VISIBLE
        }
        //底部监听
        viewModel.bottomLiveData.observe(this) {
            b.bottomNav.visibility = if (it) View.GONE else View.VISIBLE
        }
        //集数Detail监听
        viewModel.msg3LiveData.observe(this) {
            b.bottomNav.visibility = View.GONE
            val bundle = viewModel.bundle
//            if (bundle.getInt("mark") == R.id.homeFragment) return@observe
            addWithBundle(bundle, DetailedFragment::class.java)
        }
        //Search监听
        searchViewModel.searchLiveData.observe(this) {
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
            setCustomAnimations(R.anim.right_in, R.anim.right_out)
            add(R.id.layMain2, clazz, bundle, "detail")
        }

    private fun add(fragment: Fragment) {
        if (fragment == lastFragment) return
        fragmentManager.commit {
            setCustomAnimations(R.anim.right_in, R.anim.right_out)
            if (fragment.isAdded)
                show(fragment)
            else
                add(R.id.layMain2, fragment)
            hide(lastFragment)
            lastFragment = fragment
        }
    }

    override fun onBackPressed() {
        val detailedFragment = fragmentManager.findFragmentByTag("detail")
        if (detailedFragment != null) {
            fragmentManager.commit {
                setCustomAnimations(R.anim.right_in, R.anim.right_out)
                remove(detailedFragment)
            }
            Log.i("TAG", "onBackPressed: ")
        } else {
            super.onBackPressed()
        }
    }
}
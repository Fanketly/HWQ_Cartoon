package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hwq_cartoon.databinding.ActivityMainBinding
import com.example.ui.classification.SpeciesFragment
import com.example.ui.detailed.DetailedFragment
import com.example.ui.favourite.FavouriteVpFragment
import com.example.ui.home.HomeFragment
import com.example.ui.me.MeFragment
import com.example.ui.search.SearchFragment
import com.example.viewModel.CartoonViewModel

//adb connect 127.0.0.1:21503
class MainActivity : AppCompatActivity() {
    private val viewModel: CartoonViewModel by viewModels()

    private lateinit var fragmentManager: FragmentManager
    private lateinit var homeFragment: HomeFragment
    private lateinit var favouriteVpFragment: FavouriteVpFragment
    private lateinit var speciesFragment: SpeciesFragment
    private lateinit var lastFragment: Fragment
    private val meFragment by lazy { MeFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        val controller = Navigation.findNavController(this, R.id.fragCartoon)
        fragmentManager = supportFragmentManager
        homeFragment = HomeFragment()
        favouriteVpFragment = FavouriteVpFragment()
        speciesFragment = SpeciesFragment()
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .add(R.id.layMain2, homeFragment).commit()
        lastFragment = homeFragment
        //
        b.lifecycleOwner = this
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.homeFragment ->
                    add(homeFragment)
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
            if (it) b.pgMain.visibility = View.GONE
            else b.pgMain.visibility = View.VISIBLE
        }
        //底部监听
        viewModel.bottomLiveData.observe(this) {
            if (it) b.bottomNav.visibility = View.GONE
            else b.bottomNav.visibility = View.VISIBLE
        }
        //集数Detail监听
        viewModel.msg3LiveData.observe(this) {
            b.bottomNav.visibility = View.GONE
            beginTransaction(viewModel.bundle, DetailedFragment::class.java)
        }
        //Search监听
        viewModel.searchLiveData.observe(this) {
            if (!viewModel.isSearchFragment)
                when (it) {
                    1 -> {
                        b.bottomNav.visibility = View.GONE
                        beginTransaction(null, SearchFragment::class.java)
                        viewModel.isSearchFragment = true
                    }
                }
        }
        //错误监听
        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun beginTransaction(bundle: Bundle?, clazz: Class<out Fragment>) =
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .add(R.id.layMain, clazz, bundle, "detail").commit()

    private fun add(fragment: Fragment) {
        if (fragment == lastFragment) return
        if (fragment.isAdded) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .show(fragment).hide(lastFragment).commit()
        } else
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .add(R.id.layMain2, fragment).hide(lastFragment).commit()
        lastFragment = fragment
    }

    override fun onBackPressed() {
        val detailedFragment = fragmentManager.findFragmentByTag("detail")
        if (detailedFragment != null) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(detailedFragment)
                .commit()
            Log.i("TAG", "onBackPressed: ")
        } else {
            super.onBackPressed()
        }
    }
}
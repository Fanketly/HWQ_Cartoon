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
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.detailed.DetailedFragment
import com.example.hwq_cartoon.databinding.ActivityMainBinding
import com.example.search.SearchFragment
import com.example.viewModel.CartoonViewModel

//adb connect 127.0.0.1:21503
class MainActivity : AppCompatActivity() {
    val viewModel: CartoonViewModel by viewModels()
    private var itemid = R.id.homeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val controller = Navigation.findNavController(this, R.id.fragCartoon)
        NavigationUI.setupWithNavController(b.bottomNav, controller)
        controller.addOnDestinationChangedListener { _, destination, _ ->
            itemid = destination.id
            Log.i("TAG", "onCreate: $itemid")
        }
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            if (itemid != id) {
//                controller.popBackStack()
//                controller.navigate(id)
                when (id) {
                    R.id.homeFragment -> {
                        when (itemid) {
                            R.id.favoriteVpFragment ->
                                controller.navigate(R.id.action_favoriteVpFragment_to_homeFragment)
                            R.id.speciesFragment ->
                                controller.navigate(R.id.action_speciesFragment_to_homeFragment)
//                                controller.popBackStack(R.id.speciesFragment, true)
                        }
                    }
                    R.id.favoriteVpFragment -> {
                        when (itemid) {
                            R.id.homeFragment ->
                                controller.navigate(R.id.action_homeFragment_to_favoriteVpFragment)
                            R.id.speciesFragment ->
                                controller.navigate(R.id.action_speciesFragment_to_favoriteVpFragment)
                        }
                    }
                    R.id.speciesFragment -> {
                        when (itemid) {
                            R.id.homeFragment ->
                                controller.navigate(R.id.action_homeFragment_to_speciesFragment)
                            R.id.favoriteVpFragment ->
                                controller.navigate(R.id.action_favoriteVpFragment_to_speciesFragment)
                        }
                    }
                }
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
                    2 -> {

                    }
                }
        }
        //错误监听
        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun beginTransaction(bundle: Bundle?, clazz: Class<out Fragment>) =
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
            .add(R.id.layMain, clazz, bundle, "detail").commit()

    override fun onBackPressed() {
        val detailedFragment = supportFragmentManager.findFragmentByTag("detail")
        if (detailedFragment != null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out).remove(detailedFragment)
                .commit()
            Log.i("TAG", "onBackPressed: ")
        } else {
            super.onBackPressed()
        }
    }
}
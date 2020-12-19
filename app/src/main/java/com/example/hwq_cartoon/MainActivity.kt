package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.detailed.DetailedFragment
import com.example.hwq_cartoon.databinding.ActivityMainBinding
import com.example.search.SearchFragment
import com.example.viewModel.CartoonViewModel

//adb connect 127.0.0.1:21503
class MainActivity : AppCompatActivity() {
    private var itemid = R.id.homeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProvider(this)[CartoonViewModel::class.java]
        val controller = Navigation.findNavController(this, R.id.fragCartoon)
        NavigationUI.setupWithNavController(b.bottomNav, controller)
        b.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (itemid != item.itemId) {
                itemid = item.itemId
                Log.i("TAG", "setNavigation:$itemid ")
                when (itemid) {
                    R.id.homeFragment -> {
                        controller.popBackStack(R.id.favoriteVpFragment, true)
                    }
                    R.id.favoriteVpFragment -> {
                        controller.navigate(R.id.action_homeFragment_to_favoriteVpFragment)
                    }
                }

            }
            return@setOnNavigationItemSelectedListener true
        }
        //底部监听
        viewModel.bottomLiveData.observe(this) {
            if (it) b.bottomNav.visibility = View.GONE
            else b.bottomNav.visibility = View.VISIBLE
        }
        //集数Detail监听
        viewModel.msg3LiveData.observe(this) {
            viewModel.bottomLiveData.value = true
            beginTransaction(viewModel.bundle, DetailedFragment::class.java)
        }
        //Search监听
        viewModel.searchLiveData.observe(this) {
            if (!viewModel.isSearchFragment)
            if (it) {
                viewModel.bottomLiveData.value = true
                beginTransaction(null, SearchFragment::class.java)
                viewModel.isSearchFragment=true
            } else {
                Toast.makeText(this, "没找到此漫画", Toast.LENGTH_SHORT).show()
            }
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
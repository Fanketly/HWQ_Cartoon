package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.hwq_cartoon.databinding.ActivityMainBinding
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
//                        controller.navigate(R.id.action_favoriteVpFragment_to_homeFragment)
                        controller.popBackStack(R.id.favoriteVpFragment, true)
                    }
                    R.id.favoriteVpFragment -> {
                        controller.navigate(R.id.action_homeFragment_to_favoriteVpFragment)
                    }
                }

            }
            return@setOnNavigationItemSelectedListener true
        }
        viewModel.bottomLiveData.observe(this) {
            if (it) b.bottomNav.visibility = View.GONE
            else b.bottomNav.visibility = View.VISIBLE
        }

    }

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
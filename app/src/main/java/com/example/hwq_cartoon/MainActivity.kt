package com.example.hwq_cartoon

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var itemid = R.id.homeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val controller = Navigation.findNavController(this, R.id.fragCartoon)
        NavigationUI.setupWithNavController(bottom_nav, controller)
        bottom_nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (itemid != item.itemId) {
                itemid = item.itemId
                Log.i("TAG", "setNavigation:$itemid ")
                controller.popBackStack()
                controller.navigate(item.itemId)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

}
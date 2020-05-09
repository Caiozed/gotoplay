package com.caiozed.gotoplay.mainactivitypkg

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.mainactivitypkg.fragments.BacklogFragment
import com.caiozed.gotoplay.mainactivitypkg.fragments.HomeFragment
import com.caiozed.gotoplay.mainactivitypkg.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.game_layout.view.*
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    var bottomNavigation: BottomNavigationView? = null

    companion object {
        lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation!!.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance());
    }


    private var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        openFragment(HomeFragment.newInstance())
                        return true
                    }
                    R.id.navigation_sms -> {
                        openFragment(BacklogFragment.newInstance("", ""))
                        return true
                    }
                    R.id.navigation_search -> {
                        var searchFragment = SearchFragment.newInstance()
                        searchFragment.show(supportFragmentManager,
                            "add_search_dialog_fragment");
                        return true
                    }
                }
                return false
            }
        }

    fun openFragment(fragment: Fragment?) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.replace(R.id.container, fragment)
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }
}



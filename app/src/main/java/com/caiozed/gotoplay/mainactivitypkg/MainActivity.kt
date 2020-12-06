package com.caiozed.gotoplay.mainactivitypkg

import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.util.Log.d
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.mainactivitypkg.fragments.*
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.doAsyncMain
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var bottomNavigation: BottomNavigationView? = null
    var currentFragment: Fragment? = null

    companion object {
        lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        instance = this
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation!!.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation!!.itemIconTintList = null

        openFragment(HomeFragment());
    }


    private var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        openFragment(HomeFragment())
                        return true
                    }
                    R.id.navigation_backlog -> {
                        openFragment(BacklogFragment())
                        return true
                    }
                    R.id.navigation_playing -> {
                        openFragment(PlayingFragment())
                        return true
                    }
                    R.id.navigation_played -> {
                        openFragment(PlayedFragment())
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
        currentFragment = fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.replace(R.id.container, fragment)
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun openDialog(dialog: DialogFragment, positiveAction: () -> Unit, negativeAction: () -> Unit = {}, cancelable: Boolean) {
        dialog.isCancelable = cancelable
        dialog.show(supportFragmentManager, "dialog")
    }
}



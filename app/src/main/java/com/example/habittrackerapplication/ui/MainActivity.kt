package com.example.habittrackerapplication.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.ActivityMainBinding
import com.example.habittrackerapplication.ui.fragments.EditEmailFragment
import com.example.habittrackerapplication.ui.fragments.EditPasswordFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding:ActivityMainBinding
    lateinit var navHostFragment:NavHostFragment
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_nav) as NavHostFragment
        navController = navHostFragment.navController



        binding.bottomNav.setupWithNavController(navController)


        binding.navView.setNavigationItemSelectedListener(this)


        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.homeFragment || nd.id == R.id.habitListFragment
                || nd.id == R.id.calenderHistoryFragment
            ) {
                binding.toolbar.title=""
                setSupportActionBar(binding.toolbar)

                val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar, R.string.open_nav, R.string.close_nav)
                binding.drawerLayout.addDrawerListener(toggle)
                toggle.isDrawerIndicatorEnabled=true
                toggle.syncState()
                binding.bottomNav.visibility = View.VISIBLE
                binding.toolbar.visibility=View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility=View.GONE
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editEmailFragment -> {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.editEmailFragment)
            }

            R.id.editPasswordFragment -> {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.editPasswordFragment)
            }
        }
        return true

    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}
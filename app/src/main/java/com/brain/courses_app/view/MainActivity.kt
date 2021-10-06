package com.brain.courses_app.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brain.courses_app.R
import com.brain.courses_app.databinding.ActivityMainBinding
import com.brain.courses_app.databinding.NavHeaderBinding
import com.brain.courses_app.viewmodel.MainViewModel
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        mainViewModel.getUser()

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment

        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment, R.id.aboutFragment, R.id.rateUsFragment, R.id.notificationFragment),
            drawerLayout = binding.drawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.bNav.setupWithNavController(navController)

        val headerBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.apply {
            imgProNav.setOnClickListener {
                headerNavigation()
            }
            txtUnameNav.setOnClickListener {
                headerNavigation()
            }
            txtEmailNav.setOnClickListener {
                headerNavigation()
            }
            mainViewModel.userMutableLiveData.observe(this@MainActivity, {
                txtUnameNav.text = it?.name
                txtEmailNav.text = it?.email
            })
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.toolbar.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
            }
            if (destination.id == R.id.testFragment) {
                binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                binding.toolbar.setNavigationOnClickListener {
                    showDialog()
                }
            } else {
                binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                binding.toolbar.setNavigationOnClickListener {
                    navController.navigateUp(appBarConfiguration)
                }
            }
            if (destination.id == R.id.profileFragment || destination.id == R.id.homeFragment || destination.id == R.id.notificationFragment) {
                binding.bNav.visibility = View.VISIBLE
            } else {
                binding.bNav.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle(getString(R.string.leave_test))
            setCancelable(true)
            setMessage(getString(R.string.leave_test_msg))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                navController.navigateUp()
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> }
        }.create().show()
    }

    private fun headerNavigation() {
        if (navController.currentDestination?.id == R.id.profileFragment) {
            binding.drawer.closeDrawers()
        } else {
            navController.navigate(R.id.profileFragment)
            binding.drawer.closeDrawers()
        }
    }

}
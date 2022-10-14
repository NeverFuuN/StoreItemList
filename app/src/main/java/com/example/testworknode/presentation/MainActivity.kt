package com.example.testworknode.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.testworknode.R
import com.example.testworknode.databinding.ActivityMainBinding
import com.example.testworknode.utils.AppPreference
import com.google.firebase.auth.FirebaseAuth
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    lateinit var mNavController: NavController

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        APP_ACTIVITY = this
        mToolbar = binding.toolbar
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        setSupportActionBar(mToolbar)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment,R.id.loginFragment))
        AppPreference.getPreference(this)
        setupActionBarWithNavController(mNavController,appBarConfiguration)


    }
    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }
}
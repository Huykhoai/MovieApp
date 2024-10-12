package com.huynq.movieapp

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.huynq.movieapp.databinding.ActivityMainBinding
import com.huynq.movieapp.view.profile.ProfileFragment
import com.huynq.movieapp.view.favourite.FavouriteFragment
import com.huynq.movieapp.view.home.HomeFragment
import com.huynq.movieapp.view.intro.IntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        openScreen(IntroFragment())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun initView() {
         binding!!.apply {
             fab.setOnClickListener{
                 openScreen(FavouriteFragment())
             }
             btnHome.setOnClickListener {
                 openScreen(HomeFragment())
             }
             btnProfile.setOnClickListener {
                 openScreen(ProfileFragment())
             }
         }

    }

    private fun openScreen(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout_container, fragment)
        fragmentTransaction.commit()
    }
    fun setBottomNaviationVisibility(visibility: Int) {
        binding.apply {
            bottomAppBar.visibility = visibility
        }
    }
}
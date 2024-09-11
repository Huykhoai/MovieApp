package com.huynq.movieapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huynq.movieapp.view.intro.IntroFragment

class MainActivity : AppCompatActivity() {
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var fab : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initView()
        openIntro()
    }

    private fun initView() {
         coordinatorLayout = findViewById(R.id.coordinator)
         fab = findViewById(R.id.fab)
    }

    private fun openIntro() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = IntroFragment()
        fragmentTransaction.add(R.id.frameLayout_container, fragment)
        fragmentTransaction.commit()
    }
    fun setBottomNaviationVisibility(visibility: Int) {
        val isShow : Boolean = visibility == View.VISIBLE
        val translation = coordinatorLayout.height.toFloat()
        if(!isShow){
            coordinatorLayout.animate()
                .alpha(0f)
                .translationY(translation)
                .setDuration(1000)
                .withEndAction{coordinatorLayout.visibility = View.GONE}
                .start()
            fab.animate()
                .alpha(0f)
                .translationY(translation)
                .setDuration(1000)
                .withEndAction{fab.visibility = View.GONE}
                .start()
        }else{
            coordinatorLayout.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE

            coordinatorLayout.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .start()

            fab.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .start()
        }
    }
}
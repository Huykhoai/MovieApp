package com.huynq.movieapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huynq.movieapp.view.favourite.FavouriteFragment
import com.huynq.movieapp.view.intro.IntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var fab : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initView()
        openScreen(IntroFragment())
    }

    private fun initView() {
         bottomAppBar = findViewById(R.id.bottom_app_bar)
         fab = findViewById(R.id.fab)
        fab.setOnClickListener{
            openScreen(FavouriteFragment())
        }
    }

    private fun openScreen(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout_container, fragment)
        fragmentTransaction.commit()
    }
    fun setBottomNaviationVisibility(visibility: Int) {
        val isShow : Boolean = visibility == View.VISIBLE
        val translation = bottomAppBar.height.toFloat()
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout_container)
        val layoutParams = frameLayout.layoutParams as CoordinatorLayout.LayoutParams
        val marginInPx = dpToPx(60,this )
        if(!isShow){
            bottomAppBar.animate()
                .alpha(0f)
                .translationY(translation)
                .setDuration(1000)
                .withEndAction{bottomAppBar.visibility = View.GONE}
                .start()
            fab.animate()
                .alpha(0f)
                .translationY(translation)
                .setDuration(1000)
                .withEndAction{fab.visibility = View.GONE}
                .start()

            layoutParams.bottomMargin = 0
            frameLayout.layoutParams = layoutParams
        }else{
            bottomAppBar.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE

            bottomAppBar.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .start()

            fab.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .start()
            layoutParams.bottomMargin = marginInPx.toInt()
            frameLayout.layoutParams = layoutParams
        }
    }
    fun dpToPx(dp: Int, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return dp * density
    }
}
package com.huynq.movieapp.view.intro

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.huynq.movieapp.MainActivity
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentIntroBinding
import com.huynq.movieapp.view.home.HomeFragment
import com.huynq.movieapp.view.login.LoginFragment


class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIntroBinding {
        return FragmentIntroBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        animation()
    }

    private fun statusLogin() {
        val sharePreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val loginStatus = sharePreferences.getBoolean("loginStatus",false)
//        if(loginStatus){
//            openScreen(HomeFragment(),true)
//        }else{
//            openScreen(LoginFragment(),true)
//        }
        openScreen(LoginFragment(),true)
    }

    private fun initView() {
        binding!!.button.setOnClickListener {
            statusLogin()
        }
    }
    fun animation(){
        binding!!.apply {
            val fadeout = ObjectAnimator.ofFloat(imgAvatar,"alpha",1f,0.1f)
            fadeout.duration = 2000
            fadeout.interpolator = LinearInterpolator()

            val fadein = ObjectAnimator.ofFloat(imgAvatar, "alpha", 0.1f, 1f)
            fadein.duration = 2000
            fadein.interpolator = LinearInterpolator()

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(fadeout,fadein)
            animatorSet.startDelay = 500
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }
                override fun onAnimationEnd(animation: Animator) {
                    animatorSet.start()
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            animatorSet.start()
        }
    }
    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setBottomNaviationVisibility(View.GONE)
    }
}
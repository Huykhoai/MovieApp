package com.huynq.movieapp.view.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huynq.movieapp.MainActivity
import com.huynq.movieapp.R
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentIntroBinding
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
    }
    private fun initView() {
        binding!!.button.setOnClickListener {
            openScreen(LoginFragment(),true)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setBottomNaviationVisibility(View.GONE)
    }
}
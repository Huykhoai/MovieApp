package com.huynq.movieapp.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huynq.movieapp.R
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentLoginBinding
import com.huynq.movieapp.view.home.HomeFragment


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        openScreen(HomeFragment(),true)
    }
}
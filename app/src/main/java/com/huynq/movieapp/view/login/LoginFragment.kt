package com.huynq.movieapp.view.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.huynq.movieapp.R
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentLoginBinding
import com.huynq.movieapp.view.home.HomeFragment


class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private var doubleBackToExitPressedOnce: Boolean = false
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
        binding!!.btnLogin.setOnClickListener {
            openScreen(HomeFragment(),true)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(doubleBackToExitPressedOnce){
                        isEnabled = false
                        requireActivity().onBackPressed()
                        return
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireActivity().application,"Press BACK again to exit", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    },2000)
                }

            })
    }
}
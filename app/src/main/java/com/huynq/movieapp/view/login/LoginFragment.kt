package com.huynq.movieapp.view.login

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentLoginBinding
import com.huynq.movieapp.view.home.HomeFragment
import com.huynq.movieapp.viewmodel.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private var doubleBackToExitPressedOnce: Boolean = false
    private val userModel: UserModel by viewModels()
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            userModel.loginMultableLiveData.observe(viewLifecycleOwner){
                it.onSuccess {
                    openScreen(HomeFragment(), true)
                    val sharePreferences = requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE)
                    val editor = sharePreferences.edit()
                    editor.putString("user",Gson().toJson(it))
                    editor.apply()
                    Log.d("Huy", "observe: ${it.user}")
                }.onFailure{
                    showSuccessDialog(it.message)
                }
            }
        }
    }

    private fun initView() {
        binding!!.btnLogin.setOnClickListener {
            login()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                        return
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(
                        requireActivity().application,
                        "Press BACK again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }

            })
    }

    fun login() {
        val email = binding!!.editEmailLogin.text.toString().trim()
        val password = binding!!.editPasswordLogin.text.toString().trim()
        if (!validateEmail() or !validatePassword()) {
            return
        }
        userModel.login(email, password)
    }

    fun validateEmail(): Boolean {
        val email = binding!!.editEmailLogin.text.toString().trim()
        binding!!.apply {
            if (email.isEmpty()) {
                tvErrorEmail.visibility = View.VISIBLE
                tvErrorEmail.text = "Email is empty"
                return false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvErrorEmail.visibility = View.VISIBLE
                tvErrorEmail.text =
                    "Please enter the correct Email format (for example: example@gmail.com) to continue"
                return false
            }
            tvErrorEmail.visibility = View.GONE
        }
        return true
    }

    fun validatePassword(): Boolean {
        val password = binding!!.editPasswordLogin.text.toString().trim()
        binding!!.apply {
            if (password.isEmpty()) {
                tvErrorPassword.visibility = View.VISIBLE
                tvErrorPassword.text = "Password is empty"
                return false
            }
            tvErrorPassword.visibility = View.GONE
        }
        return true
    }
}
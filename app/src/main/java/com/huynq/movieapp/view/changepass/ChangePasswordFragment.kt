package com.huynq.movieapp.view.changepass

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.huynq.movieapp.R
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentChangePasswordBinding
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.login.LoginFragment
import com.huynq.movieapp.viewmodel.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    private val viewModel : UserModel by viewModels()
    private var user : UserResponse? = null
    private var old_password = false
    private var new_password = false
    private lateinit var cld: ConnectionLiveData
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangePasswordBinding {
        cld = ConnectionLiveData(requireActivity().application)
        return FragmentChangePasswordBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connect()
        initView()
    }

    private fun initView() {
        binding!!.apply {
            btnBack.setOnClickListener {
                onBack()
            }
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                   onBack()
                }

            })
        }
    }

    private fun getUser() {
        lifecycleScope.launch {
            val sharePreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            val userJson = sharePreferences.getString("user", null)
            if (userJson != null) {
                val user = Gson().fromJson(userJson, UserResponse::class.java)
                viewModel.get_user(user.user._id)
            }
            viewModel.user.observe(viewLifecycleOwner) {
                binding!!.apply {
                    if(it != null){
                        user = it
                    }
                }
            }
        }

    }
    private fun changePassword() {
        lifecycleScope.launch {
            binding!!.apply {
                btnSubmit.setOnClickListener {
                    proccessBar.visibility = View.VISIBLE
                    if(validatePassword()){
                        viewModel.change_password(user!!.user.email,editOldPassword.text.toString(),editNewPassword.text.toString())
                        viewModel.change_password.observe(viewLifecycleOwner){
                            it.onSuccess { it ->
                                proccessBar.visibility = View.GONE
                                editOldPassword.setText("")
                                editNewPassword.setText("")
                                val sharedPreferences = requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.remove("loginStatus")
                                editor.apply()
                                showDialog(it.message, "Do you want to log out?"){
                                  sharedPreferences.edit().clear().apply()
                                    openScreen(LoginFragment(),false)
                                }
                            }
                                .onFailure { it ->
                                    proccessBar.visibility = View.GONE
                                    showSuccessDialog(it.message)
                                }
                        }
                    }
                }
            }
        }
    }
    private fun connect() {
        lifecycleScope.launch {
            cld.observe(viewLifecycleOwner){
                if (it){
                    getUser()
                    changePassword()
                }
            }
        }
        binding!!.apply {
            hidePassword.setOnClickListener{
                old_password = !old_password
                if(old_password){
                    editOldPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    hidePassword.setImageResource(R.drawable.ic_hide_eye)
                    editOldPassword.setSelection(editOldPassword.text.length)
                }else{
                    editOldPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    hidePassword.setImageResource(R.drawable.ic_eye)
                    editOldPassword.setSelection(editOldPassword.text.length)
                }
            }
            hideNewPassword.setOnClickListener {
                new_password = !new_password
                if(new_password){
                    editNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    hideNewPassword.setImageResource(R.drawable.ic_hide_eye)
                    editNewPassword.setSelection(editNewPassword.text.length)
                }else{
                    editNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    hideNewPassword.setImageResource(R.drawable.ic_eye)
                    editNewPassword.setSelection(editNewPassword.text.length)
                }
            }
        }
    }

    private fun validatePassword(): Boolean{
        binding!!.apply {
            var old_pass = editOldPassword.text.toString().trim()
            var new_pass = editNewPassword.text.toString().trim()
            if(old_pass.isEmpty()){
                textError(tvErrorOldPassword,"Old password is required", View.VISIBLE)
                return false
            }
            if(new_pass.isEmpty()){
                textError(tvErrorPassword,"New password is required", View.VISIBLE)
                return false
            }
        textError(tvErrorOldPassword,"", View.GONE)
        textError(tvErrorPassword,"", View.GONE)
        return true
        }
    }
    private fun textError(textView: TextView, text: String, visibility: Int){
        textView.visibility = visibility
        textView.text = text
    }
}
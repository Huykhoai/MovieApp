package com.huynq.movieapp.view.register

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.huynq.movieapp.R
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentRegisterBinding


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private var password = false
    private var confirmPassword = false
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding!!.apply {
            hidePassword.setOnClickListener{
                password = !password
                if(password){
                    editPasswordRegister.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    hidePassword.setImageResource(R.drawable.ic_hide_eye)
                    editPasswordRegister.setSelection(editPasswordRegister.text.length)
                }else{
                    editPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    hidePassword.setImageResource(R.drawable.ic_eye)
                    editPasswordRegister.setSelection(editPasswordRegister.text.length)

                }
            }
            hideConfirmPassword.setOnClickListener{
                confirmPassword = !confirmPassword
                if(confirmPassword){
                    editConfirmPasswordRegister.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    hideConfirmPassword.setImageResource(R.drawable.ic_hide_eye)
                    editConfirmPasswordRegister.setSelection(editConfirmPasswordRegister.text.length)
                }else{
                    editConfirmPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    hideConfirmPassword.setImageResource(R.drawable.ic_eye)
                    editConfirmPasswordRegister.setSelection(editConfirmPasswordRegister.text.length)
                }
            }
        }
    }
}
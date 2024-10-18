package com.huynq.movieapp.view.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentProfileBinding
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.view.ChangePasswordFragment
import com.huynq.movieapp.view.WatchListFragment
import com.huynq.movieapp.viewmodel.UserModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: UserModel by viewModels()
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.myProfile = viewModel
        binding!!.lifecycleOwner =this
        binding!!.proccessBar.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
    }

    private fun observer() {
        val sharePreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userJson = sharePreferences.getString("user", null)
        if (userJson != null) {
            val user = Gson().fromJson(userJson, UserResponse::class.java)
            viewModel.get_user(user.user._id)
        }
        viewModel.user.observe(viewLifecycleOwner) {
            binding!!.apply {
                if(it != null){
                    proccessBar.visibility = View.GONE
                    Log.d("Huy", "observer:${it.user} ")
                    Glide.with(requireActivity()).load(it.user.avatar).into(imgProfile)
                }
            }
        }
    }

    private fun initView() {
        binding!!.apply {
            btnWatchList.setOnClickListener{
                openScreen(WatchListFragment(),true)
            }
            btnChangePass.setOnClickListener {
                openScreen(ChangePasswordFragment(), true)
            }
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                }

            })
        }
    }
}
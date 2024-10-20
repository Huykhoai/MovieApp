package com.huynq.movieapp.view.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.huynq.movieapp.adapter.WatchListAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentWatchListBinding
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.viewmodel.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WatchListFragment : BaseFragment<FragmentWatchListBinding>() {
    private val usermodel: UserModel by viewModels()
    private lateinit var watchListAdapter: WatchListAdapter
    private lateinit var cld : ConnectionLiveData
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWatchListBinding {
        cld = ConnectionLiveData(requireActivity().application)
        return FragmentWatchListBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
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

    private fun observer() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                cld.observe(viewLifecycleOwner){
                    if(it){
                        usermodel.watch_list()
                    }else{
                        showSuccessDialog("No Internet Connection")
                    }
                }
                usermodel.watchList.observe(viewLifecycleOwner){
                    if(it != null){
                        binding!!.apply {
                            recycleWatchList.apply {
                                watchListAdapter = WatchListAdapter(object : WatchListAdapter.OnclickItem{
                                    override fun OnClickItemListener(movie_id: Int) {
                                        openScreen(DetailFragment.newInstance(movie_id),true)
                                    }
                                })
                                layoutManager = GridLayoutManager(requireContext(),2)
                                adapter = watchListAdapter
                                watchListAdapter.updateData(it.data)

                            }
                        }
                    }
                }
            }
        }
    }
}
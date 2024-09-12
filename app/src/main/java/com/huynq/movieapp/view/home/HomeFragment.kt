package com.huynq.movieapp.view.home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.huynq.movieapp.MainActivity
import com.huynq.movieapp.R
import com.huynq.movieapp.adapter.HomeAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.databinding.FragmentHomeBinding
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.utils.APIConstants
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.view.search.SearchFragment
import com.huynq.movieapp.viewmodel.MainViewModel
import com.huynq.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    private lateinit var mainViewModel: MainViewModel
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        var responsitory  = MovieResponsitory()
        val viewModelFactory = MainViewModelFactory(responsitory)
        mainViewModel =ViewModelProvider(requireActivity(),viewModelFactory)[MainViewModel::class.java]
        return FragmentHomeBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.progressBarNewMovie?.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        initView()
        displayPopularMovies()
        displayUpCommingMovies()

    }

    private fun initView() {
        binding?.editSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
         override fun onQueryTextSubmit(query: String?): Boolean {
             if(query != null && query.isNotEmpty()){
                openScreen(SearchFragment.newInstance(query),true)
             }
             return true
         }

         override fun onQueryTextChange(newText: String?): Boolean {
              return false
         }

     })
    }
    private fun displayPopularMovies(){
        lifecycleScope.launch {
            mainViewModel.getPopularMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main){
                mainViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null){
                        binding?.progressBarNewMovie?.visibility = View.GONE
                        binding?.progressBarUpcommingMovie?.visibility = View.GONE
                        binding?.recycleViewNewMovie?.adapter = HomeAdapter(
                            it.results,
                            mainViewModel,
                            object : HomeAdapter.MovieListRVAdapterClickListener{
                                override fun onMovieClick(movie_id: Int) {
                                    openScreen(DetailFragment.newInstance(movie_id), true)
                                }


                            })
                        binding?.recycleViewNewMovie?.layoutManager =
                            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                    }
                })
            }
        }
    }
    private fun displayUpCommingMovies(){
        binding?.progressBarUpcommingMovie?.visibility = View.VISIBLE
        lifecycleScope.launch {
            mainViewModel.getUpCommingMovies(APIConstants.API_KEY)
            withContext(Dispatchers.Main){
                mainViewModel.upcommingMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null){
                        binding?.progressBarUpcommingMovie?.visibility = View.GONE
                        binding?.recycleViewUpcommingMovie?.adapter = HomeAdapter(
                            it.results,
                            mainViewModel,
                            object : HomeAdapter.MovieListRVAdapterClickListener{
                                override fun onMovieClick(movie_id: Int) {
                                    openScreen(DetailFragment.newInstance(movie_id), true)
                                }
                            })
                        binding?.recycleViewUpcommingMovie?.layoutManager =
                            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                    }
                })
            }
        }
    }
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setBottomNaviationVisibility(View.VISIBLE)
    }
}
package com.huynq.movieapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.huynq.movieapp.MainActivity
import com.huynq.movieapp.adapter.DiscoverMovieHomeAdapter
import com.huynq.movieapp.adapter.HomeAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentHomeBinding
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.discoverMovie.DiscoverMoviesFragment
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.view.search.SearchFragment
import com.huynq.movieapp.viewmodel.MainViewModel
import com.huynq.movieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    private val mainViewModel: MainViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    @Inject
    lateinit var discoverMovieAdapter: DiscoverMovieHomeAdapter
    private lateinit var cld : ConnectionLiveData
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        cld = ConnectionLiveData(requireActivity().application)
        return FragmentHomeBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.apply {
            progressBarNewMovie.visibility = View.VISIBLE
            progressBarUpcommingMovie.visibility = View.VISIBLE
            progressBarDiscoverMovie.visibility = View.VISIBLE
            progressBarToprateMovie.visibility = View.VISIBLE
        }
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAPiCall()
    }
    private fun initView() {
        binding!!.apply {
            editSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
            btnSeeall.setOnClickListener {
                openScreen(
                    DiscoverMoviesFragment(),
                    true
                )}
        }
    }
    private fun initAPiCall() {
        cld.observe(viewLifecycleOwner) { isConnected ->
            if(isConnected){
                mainViewModel.getPopularMovies()
                mainViewModel.getUpCommingMovies()
                mainViewModel.getTopRateMovies()
                displayPopularMovies()
                displayUpCommingMovies()
                displayDiscoverMovies()
                displayToprateMovies()
            }
        }
    }
    private fun displayPopularMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                mainViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null){
                        binding!!.apply {
                            progressBarNewMovie.visibility = View.GONE
                            recycleViewNewMovie.apply {
                                adapter = HomeAdapter(
                                    it.results,
                                    mainViewModel,
                                    object : HomeAdapter.MovieListRVAdapterClickListener{
                                        override fun onMovieClick(movie_id: Int) {
                                            openScreen(DetailFragment.newInstance(movie_id), true)
                                        }
                                    })
                                recycleViewNewMovie.layoutManager =
                                    LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                            }
                        }
                    }
                })
            }
        }
    }
    private fun displayUpCommingMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                mainViewModel.upcommingMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null){
                        binding!!.apply {
                            progressBarUpcommingMovie.visibility = View.GONE
                            recycleViewUpcommingMovie.apply {
                                adapter = HomeAdapter(
                                    it.results,
                                    mainViewModel,
                                    object : HomeAdapter.MovieListRVAdapterClickListener{
                                        override fun onMovieClick(movie_id: Int) {
                                            openScreen(DetailFragment.newInstance(movie_id), true)
                                        }
                                    })
                                recycleViewUpcommingMovie.layoutManager =
                                    LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                            }
                        }
                    }
                })
            }
        }
    }
    private fun displayDiscoverMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
               movieViewModel.moviesResult.collectLatest{ response ->
                   binding!!.apply {
                       progressBarDiscoverMovie.visibility = View.GONE
                       discoverMovieAdapter = DiscoverMovieHomeAdapter()
                       discoverMovieAdapter.setOnclickItem(
                           object : DiscoverMovieHomeAdapter.MovieListRVAdapterClickListener{
                               override fun onMovieClick(movie_id: Int) {
                                   openScreen(DetailFragment.newInstance(movie_id),true)
                               }

                           }
                       )
                       recycleViewDiscoverMovie.apply {
                           adapter = discoverMovieAdapter
                           layoutManager = LinearLayoutManager(
                               activity,
                               LinearLayoutManager.HORIZONTAL,
                               false
                           )
                       }
                   }
                   discoverMovieAdapter.submitData(response)
               }
            }
        }
    }
    private fun displayToprateMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                mainViewModel.topRateMoviesLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null){
                        binding!!.apply {
                            progressBarToprateMovie.visibility = View.GONE
                            recycleViewToprateMovie.apply {
                                adapter = HomeAdapter(
                                    it.results,
                                    mainViewModel,
                                    object : HomeAdapter.MovieListRVAdapterClickListener{
                                        override fun onMovieClick(movie_id: Int) {
                                            openScreen(DetailFragment.newInstance(movie_id), true)
                                        }
                                    })
                                recycleViewToprateMovie.layoutManager =
                                    LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                            }
                        }
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
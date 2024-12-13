package com.huynq.movieapp.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.huynq.movieapp.MainActivity
import com.huynq.movieapp.adapter.BannerAdapter
import com.huynq.movieapp.adapter.DiscoverMovieHomeAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentHomeBinding
import com.huynq.movieapp.model.Banner
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.view.discoverMovie.DiscoverMoviesFragment
import com.huynq.movieapp.viewmodel.MainViewModel
import com.huynq.movieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    private val mainViewModel: MainViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var cld : ConnectionLiveData
    private lateinit var handle: Handler
    private lateinit var runnable : Runnable
    private val listImage: MutableList<Banner> = mutableListOf()
    private var doubleBackToExitPressedOnce: Boolean = false
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        cld = ConnectionLiveData(requireActivity().application)
        return FragmentHomeBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAPiCall()
    }
    private fun startViewPager(){
        binding!!.apply {
            handle = Handler(Looper.getMainLooper())
            runnable = Runnable{
                var current = viewpager.currentItem
                if(current == 4) {
                    viewpager.currentItem = 0
                } else {
                    viewpager.currentItem += 1
                }
            }
        }
    }
    private fun stopAutoViewPager() {
        if (handle != null && runnable != null) {
            handle.removeCallbacks(runnable)
        }
    }
    private fun banner(list: MovieResponse) {
        binding!!.apply {
            bannerAdapter = BannerAdapter(
                requireContext(),
                object : BannerAdapter.OnClickBannerListener{
                    override fun OnClickItemListener(movie_id: Int) {
                        openScreen(DetailFragment.newInstance(movie_id),true)
                    }
                })
            viewpager.adapter = bannerAdapter
            for (image in list.results){
                val banner = Banner(image.id,image.poster_path,image.backdrop_path)
                listImage.add(banner)
            }
            bannerAdapter.updateData(listImage)
            viewpager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handle.removeCallbacks(runnable)
                    handle.postDelayed(runnable,3000)
                }
            })
        }
    }

    private fun initView() {
        binding!!.apply {
            btnSeeall.setOnClickListener {
                openScreen(
                    DiscoverMoviesFragment(),
                    true
                )}
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
    private fun initAPiCall() {
        cld.observe(viewLifecycleOwner) { isConnected ->
            if(isConnected){
                mainViewModel.getNowPlaying()
//                mainViewModel.getPopularMovies()
//                mainViewModel.getUpCommingMovies()
//                mainViewModel.getTopRateMovies()
                displayNowPlaying()
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
               movieViewModel.moviesResult.collectLatest { response ->
                   if(response != null){
                       binding!!.apply {
                           progressBarNewMovie.visibility = View.GONE
                           recycleViewNewMovie.apply {
                               var discoverMovieAdapter = DiscoverMovieHomeAdapter()
                               discoverMovieAdapter.setOnclickItem(
                                   object : DiscoverMovieHomeAdapter.MovieListRVAdapterClickListener{
                                       override fun onMovieClick(movie_id: Int) {
                                           openScreen(DetailFragment.newInstance(movie_id),true)
                                       }
                                   }
                               )
                               recycleViewNewMovie.apply {
                                   adapter = discoverMovieAdapter
                                   layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                               }
                               discoverMovieAdapter.submitData(response)
                           }
                       }
                   }
               }
            }
        }
    }
    private fun displayUpCommingMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                movieViewModel.upcomingMovieResult.collectLatest{response ->
                    if(response != null){
                        binding!!.apply {
                            progressBarUpcommingMovie.visibility = View.GONE
                            var upcommingMovieAdapter = DiscoverMovieHomeAdapter()
                            recycleViewUpcommingMovie.apply {
                                upcommingMovieAdapter.setOnclickItem(
                                    object : DiscoverMovieHomeAdapter.MovieListRVAdapterClickListener{
                                        override fun onMovieClick(movie_id: Int) {
                                            openScreen(DetailFragment.newInstance(movie_id),true)
                                        }

                                    }
                                )
                                adapter = upcommingMovieAdapter
                                layoutManager =
                                    LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                            }
                            upcommingMovieAdapter.submitData(response)
                        }
                    }
                }
            }
        }
    }
    private fun displayDiscoverMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
               movieViewModel.moviesResult.collectLatest{ response ->
                   binding!!.apply {
                       progressBarDiscoverMovie.visibility = View.GONE
                       var discoverMovieAdapter = DiscoverMovieHomeAdapter()
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
                       discoverMovieAdapter.submitData(response)
                   }
               }
            }
        }
    }
    private fun displayToprateMovies(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
               movieViewModel.topRateMovieResult.collectLatest { response->
                   if(response != null){
                       binding!!.apply {
                           progressBarToprateMovie.visibility = View.GONE
                           var discoverMovieAdapter = DiscoverMovieHomeAdapter()
                           recycleViewToprateMovie.apply {
                               discoverMovieAdapter.setOnclickItem(
                                   object : DiscoverMovieHomeAdapter.MovieListRVAdapterClickListener{
                                       override fun onMovieClick(movie_id: Int) {
                                           openScreen(DetailFragment.newInstance(movie_id), true)
                                       }
                                   })
                               layoutManager =
                                   LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                               adapter = discoverMovieAdapter
                           }
                           discoverMovieAdapter.submitData(response)
                       }
                   }
               }
            }
        }
    }
    private fun displayNowPlaying(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                mainViewModel.nowPlayingLiveData.observe(viewLifecycleOwner) {
                    if(it != null){
                        binding!!.proccessBarNowplaying.visibility = View.GONE
                        banner(it)
                        binding!!.circleIndicator.setViewPager(binding!!.viewpager)
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setBottomNaviationVisibility(View.VISIBLE)
        binding!!.apply {
            progressBarNewMovie.visibility = View.VISIBLE
            progressBarUpcommingMovie.visibility = View.VISIBLE
            progressBarDiscoverMovie.visibility = View.VISIBLE
            progressBarToprateMovie.visibility = View.VISIBLE
            proccessBarNowplaying.visibility = View.VISIBLE
        }
    }
    override fun onResume() {
        super.onResume()
        startViewPager()
    }

    override fun onPause() {
        super.onPause()
        stopAutoViewPager()
    }
}
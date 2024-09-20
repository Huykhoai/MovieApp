package com.huynq.movieapp.view.discoverMovie

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.huynq.movieapp.adapter.DiscoverMovieAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentDiscoverMoviesBinding
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class DiscoverMoviesFragment : BaseFragment<FragmentDiscoverMoviesBinding>() {
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var cld : ConnectionLiveData
    @Inject
    lateinit var adapterDiscoverMovie: DiscoverMovieAdapter
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDiscoverMoviesBinding {
        cld = ConnectionLiveData(requireActivity().application)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapterDiscoverMovie.loadStateFlow.collect {
                    binding!!.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding!!.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
       return FragmentDiscoverMoviesBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.proccessBar.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        initAPiCall()
    }
    private fun initAPiCall() {
        cld.observe(viewLifecycleOwner) { isConnected ->
            if(isConnected){
                lifecycleScope.launch {
                    movieViewModel.moviesResult.collectLatest {response ->
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            binding!!.proccessBar.visibility =View.GONE
                        },500)
                        binding!!.apply {
                            adapterDiscoverMovie.submitData(response)

                        }
                    }
                }
            }
        }
    }
    private fun setupRecycleView() {
        binding!!.apply {
            recycleViewDiscoverMovies.apply {
                layoutManager = GridLayoutManager(activity,2)
                adapter = adapterDiscoverMovie
                adapterDiscoverMovie.setOnclickItem(object : DiscoverMovieAdapter.MovieListRVAdapterClickListener {
                    override fun onMovieClick(movie_id: Int) {
                       openScreen(DetailFragment.newInstance(movie_id),true)
                    }

                })
            }
        }
    }
}
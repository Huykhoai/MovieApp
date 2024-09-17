package com.huynq.movieapp.view.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.R
import com.huynq.movieapp.adapter.CastAdapter
import com.huynq.movieapp.adapter.TrailersMovieAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.databinding.FragmentDetailBinding
import com.huynq.movieapp.model.Genre
import com.huynq.movieapp.utils.APIConstants
import com.huynq.movieapp.viewmodel.MainViewModel
import com.huynq.movieapp.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private lateinit var mainViewModel: MainViewModel
    private val trailerPaths = mutableListOf<String>()
    private var movie_id : Int? = null
    companion object{
        @JvmStatic
        fun newInstance(movie_id: Int): DetailFragment{
            val args = Bundle()
            args.putInt("movie_id",movie_id)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
            }
    }
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        movie_id = arguments?.getInt("movie_id")
        val responsitory = MovieResponsitory()
        val factory = MainViewModelFactory(responsitory)
        mainViewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]
       return FragmentDetailBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.detailLoading?.visibility = View.VISIBLE
        Glide.with(this).clear(binding?.posterNomalImg!!)
        Glide.with(this).clear(binding?.posterNomalImg!!)
        super.onViewCreated(view, savedInstanceState)
        initView()
        displayDetailMovies()
        displayVideoMovies()
        displayCastMovies()
    }

    private fun initView() {
        binding?.btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun displayDetailMovies() {
        lifecycleScope.launch {
            if (movie_id != null) {
                mainViewModel.getDetailMovies(movie_id!!)
            }
        }
        mainViewModel.detailMoviesLiveData.observe(viewLifecycleOwner, Observer {
             it.poster_path?.let { posterPath ->
                 val imageUrl = APIConstants.IMAGE_PATH + posterPath
                 Glide.with(binding?.posterNomalImg!!)
                     .load(imageUrl)
                     .into(binding?.posterNomalImg!!)
                 Log.i("Retrofit", "Poster url = $imageUrl")
             }
             it.backdrop_path?.let { backdropPath ->
                 val imageUrl = APIConstants.IMAGE_PATH + backdropPath
                 Glide.with(binding?.posterBigImg!!)
                     .load(imageUrl)
                     .into(binding?.posterBigImg!!)
                 Log.i("Retrofit", "Backdrop url = $imageUrl")
             }

             binding?.title?.text = it.title
             binding?.movieTime?.text = convertMinutesToTimeFormat(it.runtime)
             binding?.movieDate?.text = it.release_date
             binding?.currentStar?.text = it.vote_average.toString().substring(0,3)
             binding?.movieSummaryInfo?.text = it.overview
             binding?.movieSummaryInfo?.ellipsize = TextUtils.TruncateAt.MARQUEE
             binding?.movieActorInfo?.text = displayGenre(it.genres)
             checkDataLoaded()
        })
    }
    fun displayVideoMovies(){
        lifecycleScope.launch {
            if (movie_id != null) {
                mainViewModel.getVideosMovies(movie_id!!)
            }
            mainViewModel.videoMoviesLiveData.observe(viewLifecycleOwner, Observer {
                trailerPaths.clear()
                for (videoItem in it.results) {
                    if (videoItem.type.equals("Trailer", ignoreCase = true)) {
                        trailerPaths.add(videoItem.key)
                    }
                }
                if(!trailerPaths.isNullOrEmpty()){
                     binding?.recyclerViewTrailer?.layoutManager = LinearLayoutManager(
                             requireContext(),
                             LinearLayoutManager.HORIZONTAL,
                             false
                         )
                     binding?.recyclerViewTrailer?.adapter = TrailersMovieAdapter(trailerPaths)
                }
                checkDataLoaded()
            })

        }
    }
    fun displayCastMovies(){
        lifecycleScope.launch {
            if(movie_id != null){
                mainViewModel.getCastsMovies(movie_id!!)
            }
            mainViewModel.castMoviesLiveData.observe(viewLifecycleOwner,Observer{
                if(!it.cast.isNullOrEmpty()){
                    binding?.recyclerViewCast?.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding?.recyclerViewCast?.adapter = CastAdapter(it.cast)
                }
                checkDataLoaded()
            })

        }

    }
    fun displayGenre(genres: List<Genre>): String {
        var genre = ""
        for (element in genres) {
            genre += element.name + ", "
        }
        return genre.substring(0, genre.length-2)
    }
    fun convertMinutesToTimeFormat(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        val seconds = 0

        return String.format("%02d:%02d:%02d", hours, remainingMinutes, seconds)
    }
    private fun checkDataLoaded() {
        if (mainViewModel.detailMoviesLiveData.value != null &&
            mainViewModel.castMoviesLiveData.value != null &&
            mainViewModel.videoMoviesLiveData.value != null) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding?.detailLoading?.visibility = View.GONE
            }, 500)
        }
    }
    override fun onStart() {
        super.onStart()

    }
}
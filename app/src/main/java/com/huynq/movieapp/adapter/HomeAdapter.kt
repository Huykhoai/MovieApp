package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ViewholderFilmBinding
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.utils.APIConstants
import com.huynq.movieapp.viewmodel.MainViewModel

class HomeAdapter(
    private val movies: List<Movies>,
    private val mainViewModel: MainViewModel,
    private val movieListRVAdapterClickListener: MovieListRVAdapterClickListener
): RecyclerView.Adapter<HomeAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderFilmBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movies = movies[position]
        holder.bind(movies)
        holder.itemView.setOnClickListener {
           if(movieListRVAdapterClickListener != null){
               movieListRVAdapterClickListener.onMovieClick(movies.id)
           }
        }
    }
    inner class MovieViewHolder(private val binding: ViewholderFilmBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(movie: Movies){
           val imageUrl = APIConstants.IMAGE_PATH + movie.poster_path
           binding.title.text = movie.title
           binding.title.maxLines = 2
           binding.title.ellipsize = TextUtils.TruncateAt.END
           Glide.with(binding.pic.context).load(imageUrl).into(binding.pic)
           binding.score.text = movie.vote_average.toString().substring(0,3)
       }
    }
    interface MovieListRVAdapterClickListener{
       fun onMovieClick(movie_id: Int)
    }
}
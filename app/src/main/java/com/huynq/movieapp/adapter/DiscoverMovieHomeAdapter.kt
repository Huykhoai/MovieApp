package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ViewholderFilmBinding
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.utils.APIConstants
import javax.inject.Inject

class DiscoverMovieHomeAdapter @Inject constructor() :
    PagingDataAdapter<Movies, DiscoverMovieHomeAdapter.ViewHolder>(Diff()) {
    private lateinit var movieListRVAdapterClickListener: MovieListRVAdapterClickListener

    fun setOnclickItem(movieListRVAdapterClickListener: MovieListRVAdapterClickListener) {
        this.movieListRVAdapterClickListener = movieListRVAdapterClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                if (movieListRVAdapterClickListener != null) {
                    movieListRVAdapterClickListener!!.onMovieClick(movie.id)
                }
            }
        }
    }

    inner class ViewHolder(private val binding: ViewholderFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movies) {
            val imageUrl = APIConstants.IMAGE_PATH + movie.poster_path
            binding.title.text = movie.title
            binding.title.maxLines = 2
            binding.title.ellipsize = TextUtils.TruncateAt.END
            Glide.with(binding.pic.context).load(imageUrl).into(binding.pic)
            binding.score.text = movie.vote_average.toString().substring(0, 3)
        }
    }

    class Diff : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean =
            oldItem == newItem
    }

    interface MovieListRVAdapterClickListener {
        fun onMovieClick(movie_id: Int)
    }
}
package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ItemSearchMovieBinding
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.utils.APIConstants

class WatchListAdapter(
    private val onclickItem: OnclickItem
): RecyclerView.Adapter<WatchListAdapter.ViewHolder>(){
    private var movies: List<Movies> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListAdapter.ViewHolder {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListAdapter.ViewHolder, position: Int) {
        var movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onclickItem.OnClickItemListener(movie.id)
        }
    }

    override fun getItemCount(): Int {
       return movies.size
    }
    inner class ViewHolder(private val binding: ItemSearchMovieBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(movie: Movies){
                val imageUrl = APIConstants.IMAGE_PATH + movie.poster_path
                binding.title.text = movie.title
                binding.title.maxLines = 2
                binding.title.ellipsize = TextUtils.TruncateAt.END
                Glide.with(binding.pic.context).load(imageUrl).into(binding.pic)
                binding.score.text = movie.vote_average.toString().substring(0,3)
            }
    }
    fun updateData(movies: List<Movies>){
        this.movies = movies
        notifyDataSetChanged()
    }
    interface OnclickItem{
        fun OnClickItemListener(movie_id: Int)
    }
}
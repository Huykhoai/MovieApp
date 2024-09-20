package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.adapter.SearchAdapter.OnClickItemListener
import com.huynq.movieapp.databinding.ItemSearchMovieBinding
import com.huynq.movieapp.room.FavouriteMovie
import com.huynq.movieapp.utils.APIConstants
import javax.inject.Inject

class FavouriteAdapter @Inject constructor(
    var movies: List<FavouriteMovie>,
    private val onClckItemListener: OnClickItemListener
) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }
    inner class ViewHolder(private val binding: ItemSearchMovieBinding)
        :RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: FavouriteMovie){
            binding.title.text = movie.movie_name
            binding.title.maxLines = 2
            binding.title.ellipsize = TextUtils.TruncateAt.END
            Glide.with(binding.pic.context).load(movie.movie_image).into(binding.pic)
            binding.score.text = movie.movie_vote.toString().substring(0,3)

            itemView.setOnClickListener{
                if(onClckItemListener != null){
                    onClckItemListener.onClickItem(movie.movie_id)
                }
            }
        }
    }
    interface OnClickItemListener{
        fun onClickItem(movie_id: Int)
    }
}
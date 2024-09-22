package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ItemSearchMovieBinding
import com.huynq.movieapp.room.FavouriteMovie
import javax.inject.Inject

class FavouriteAdapter @Inject constructor(
    private var isChooseAll: Boolean,
    private var isClickChoose: Boolean,
    private val onClckItemListener: OnClickItemListener
) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    private var movies: List<FavouriteMovie> = emptyList()
    private var selectedList : MutableList<FavouriteMovie> = mutableListOf()
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
            binding.apply {
                title.text = movie.movie_name
                title.maxLines = 2
                title.ellipsize = TextUtils.TruncateAt.END
                Glide.with(binding.pic.context).load(movie.movie_image).into(pic)
                score.text = movie.movie_vote.toString().substring(0,3)

                if(isChooseAll){
                    checkbox.visibility = View.VISIBLE
                    checkbox.isChecked = isChooseAll
                }else{
                    checkbox.visibility = View.GONE
                    checkbox.isChecked = isChooseAll
                }
                if(!isClickChoose){
                    checkbox.visibility = View.GONE
                    checkbox.isChecked = false
                }
                checkbox.setOnCheckedChangeListener{ _, isChecked ->
                    if(isChecked){
                        selectedList.add(movie)
                    }else{
                        selectedList.remove(movie)
                        checkbox.visibility = View.GONE
                    }
                }
            }
            itemView.setOnClickListener{
                if(isClickChoose){
                    binding.apply {
                        checkbox.visibility = View.VISIBLE
                        checkbox.isChecked = isClickChoose
                    }
                }else{
                    binding.apply {
                        checkbox.visibility = View.GONE
                        checkbox.isChecked = isClickChoose
                    }
                    if(onClckItemListener != null){
                        onClckItemListener.onClickItem(movie)
                    }
                }
            }
        }
    }
    interface OnClickItemListener{
        fun onClickItem(movie: FavouriteMovie)
    }
    fun updateMovies(newMovies: List<FavouriteMovie>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
    fun updateStatus(isCheck : Boolean){
        this.isClickChoose = isCheck
        notifyDataSetChanged()
    }
    fun updateStatusChooseAll(isCheck : Boolean){
        this.isChooseAll = isCheck
        notifyDataSetChanged()
    }
    fun getSelectedList() : MutableList<FavouriteMovie> = selectedList
}
package com.huynq.movieapp.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ItemSearchMovieBinding
import com.huynq.movieapp.model.SearchResult
import com.huynq.movieapp.utils.APIConstants

class SearchAdapter(
    var listSearch: List<SearchResult>,
    private val onClckItemListener: OnClickItemListener
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSearch.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResult = listSearch[position]
        holder.bind(searchResult)
    }
    inner class ViewHolder(private val binding: ItemSearchMovieBinding): RecyclerView.ViewHolder(binding.root) {
         fun bind(searchResult: SearchResult){
             val imageUrl = APIConstants.IMAGE_PATH + searchResult.poster_path
             binding.title.text = searchResult.title
             binding.title.maxLines = 2
             binding.title.ellipsize = TextUtils.TruncateAt.END
             Glide.with(binding.pic.context).load(imageUrl).into(binding.pic)
             binding.score.text = searchResult.vote_average.toString().substring(0,3)

             itemView.setOnClickListener{
                 if(onClckItemListener != null){
                     onClckItemListener.onClickItem(searchResult.id)
                 }
             }
         }
    }
    interface OnClickItemListener{
        fun onClickItem(movie_id: Int)
    }
}
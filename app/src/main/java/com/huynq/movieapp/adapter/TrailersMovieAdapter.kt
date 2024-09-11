package com.huynq.movieapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.huynq.movieapp.databinding.ItemTrailerVideoBinding
import com.huynq.movieapp.utils.APIConstants

class TrailersMovieAdapter(private val trailers: List<String>) : RecyclerView.Adapter<TrailersMovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrailerVideoBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = trailers[position]
        holder.bind(trailer)
    }
    inner class ViewHolder(private val binding: ItemTrailerVideoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(trailer: String){
           if(!trailer.isNullOrEmpty()){
               binding.trailerWedview.apply {
                   clearHistory()
                   loadData("", "text/html", "utf-8")
                   settings.javaScriptEnabled = true
                   setBackgroundColor(Color.TRANSPARENT)
                   val html =
                       "<iframe width =\"100%\" height =\"100%\" src=\"${APIConstants.YOUTUBE_URL}${trailer}\" frameborder=\"0\" allowfullscreen></iframe>"
                   loadData(html, "text/html","utf-8")
               }
           }else{
               Toast.makeText(itemView.context, "Unable to fetch trailers!", Toast.LENGTH_SHORT).show()
           }
        }
    }
}
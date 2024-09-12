package com.huynq.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.R
import com.huynq.movieapp.databinding.ItemCastBinding
import com.huynq.movieapp.model.Cast
import com.huynq.movieapp.utils.APIConstants

class CastAdapter(private val casts : List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cast = casts[position]
        holder.bind(cast)
    }
    inner class ViewHolder(private val binding : ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast : Cast){
            binding.castName.text = cast.name
          if(cast.profile_path != null){
              val imageUrl = APIConstants.IMAGE_PATH+cast.profile_path
              Glide.with(binding.castMemberImageView.context)
                  .load(imageUrl)
                  .into(binding.castMemberImageView)
          }else{
              Glide.with(binding.castMemberImageView.context)
                  .load(R.drawable.no_image_placeholder)
                  .into(binding.castMemberImageView)
          }
        }
    }
}
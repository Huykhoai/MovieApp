package com.huynq.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ItemBannerBinding
import com.huynq.movieapp.model.Banner
import com.huynq.movieapp.utils.APIConstants

class BannerAdapter(
    private val context: Context,
    val listener: OnClickBannerListener)
    : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {


    private var bannerList: MutableList<Banner> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bannerList[position])
        holder.itemView.setOnClickListener {
            if(listener != null){
                listener.OnClickItemListener(bannerList[position].movie_id)
            }
        }
    }

    class ViewHolder(private val binding: ItemBannerBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(banner: Banner) {
            val imageBackdrop = APIConstants.IMAGE_PATH + banner.backdrop_path
            val imagePoster = APIConstants.IMAGE_PATH + banner.poster_path
            Glide.with(binding.posterBigImg.context)
                .load(imageBackdrop)
                .into(binding.posterBigImg)
            Glide.with(binding.posterNomalImg.context)
                .load(imagePoster)
                .into(binding.posterNomalImg)
        }

    }
    interface OnClickBannerListener {
        fun OnClickItemListener(movie_id: Int)
    }
    fun updateData(newList: MutableList<Banner>) {
        this.bannerList = newList
        notifyDataSetChanged()
    }
//    override fun getCount(): Int {
//        return bannerList.size
//    }
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val binding = ItemBannerBinding.inflate(LayoutInflater.from(context), container, false)
//        val banner = bannerList[position]
//        val imageBackdrop = APIConstants.IMAGE_PATH + banner.backdrop_path
//        val imagePoster = APIConstants.IMAGE_PATH + banner.poster_path
//        Glide.with(binding.posterBigImg.context)
//            .load(imageBackdrop)
//            .into(binding.posterBigImg)
//        Glide.with(binding.posterNomalImg.context)
//            .load(imagePoster)
//            .into(binding.posterNomalImg)
//        container.addView(binding.root)
//        return binding.root
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//    }
}
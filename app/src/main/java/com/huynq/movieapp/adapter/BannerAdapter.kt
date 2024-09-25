package com.huynq.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.huynq.movieapp.databinding.ItemBannerBinding
import com.huynq.movieapp.model.Banner
import com.huynq.movieapp.utils.APIConstants

class BannerAdapter(private val context: Context) : PagerAdapter() {

    private var bannerList: MutableList<Banner> = mutableListOf()

    fun updateData(newList: MutableList<Banner>) {
        this.bannerList = newList
        notifyDataSetChanged()
    }
    override fun getCount(): Int {
        return bannerList.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(context), container, false)
        val banner = bannerList[position]
        val imageBackdrop = APIConstants.IMAGE_PATH + banner.backdrop_path
        val imagePoster = APIConstants.IMAGE_PATH + banner.poster_path
        Glide.with(binding.posterBigImg.context)
            .load(imageBackdrop)
            .into(binding.posterBigImg)
        Glide.with(binding.posterNomalImg.context)
            .load(imagePoster)
            .into(binding.posterNomalImg)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
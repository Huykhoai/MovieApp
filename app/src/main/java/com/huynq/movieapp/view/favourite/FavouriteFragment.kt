package com.huynq.movieapp.view.favourite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.huynq.movieapp.R
import com.huynq.movieapp.adapter.FavouriteAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentFavouriteBinding
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.viewmodel.FavouriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    private val viewModel: FavouriteMovieViewModel by viewModels()
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(layoutInflater,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayFavouriteMovies()
    }

    private fun displayFavouriteMovies() {
        lifecycleScope.launch {
            viewModel.favouriteMovies.observe(viewLifecycleOwner) { response->
                if(response != null && response.isNotEmpty()){
                    Log.d("Dao", "displayFavouriteMovies:${response.get(0).movie_id} ")
                    binding!!.apply {
                        titleEmpty.visibility = View.GONE
                        recycleViewDiscoverMovies.visibility = View.VISIBLE
                        recycleViewDiscoverMovies.adapter = FavouriteAdapter(
                            response,
                            object : FavouriteAdapter.OnClickItemListener{
                                override fun onClickItem(movie_id: Int) {
                                    openScreen(DetailFragment.newInstance(movie_id),true)
                                }

                            }
                        )
                        recycleViewDiscoverMovies.layoutManager =
                            GridLayoutManager(requireContext(), 2)
                    }
                }else{
                    binding!!.apply {
                        titleEmpty.visibility = View.VISIBLE
                        recycleViewDiscoverMovies.visibility = View.GONE
                    }
                }
            }

        }
    }
}
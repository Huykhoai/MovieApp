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
import com.huynq.movieapp.room.FavouriteMovie
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.view.home.HomeFragment
import com.huynq.movieapp.viewmodel.FavouriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    private val viewModel: FavouriteMovieViewModel by viewModels()
    private lateinit var favouriteAdapter: FavouriteAdapter
    private var isClickChoose: Boolean = false
    private var isChooseAll: Boolean = false
    private var selectedList : MutableList<FavouriteMovie> = mutableListOf()
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(layoutInflater,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        initView()
        displayFavouriteMovies()
    }

    private fun initView() {
      binding!!.apply {
          btnBack.setOnClickListener{
              openScreen(HomeFragment(),true)
          }
          btnChoose.setOnClickListener {
              isClickChoose = !isClickChoose
              favouriteAdapter.updateStatus(isClickChoose)
              if(isClickChoose){
                  btnChoose.text = "Cancel"
                  btnDelete.visibility = View.VISIBLE
                  btnChooseAll.visibility = View.VISIBLE
              }else{
                  btnChoose.text = "Choose"
                  btnDelete.visibility = View.GONE
                  btnChooseAll.visibility = View.GONE
                  favouriteAdapter.updateStatus(isClickChoose)
              }
          }
          btnDelete.setOnClickListener {
              selectedList = favouriteAdapter.getSelectedList()
              if(selectedList.isNotEmpty()){
                  showDialog("Delete movie",
                      "Are you sure you want to delete this movie?"
                  ) {
                      for (movie in selectedList) {
                          viewModel.delete(movie)
                      }
                  }

              }
          }
          btnChooseAll.setOnCheckedChangeListener{_, isChecked ->
              if(isChecked){
                  isChooseAll = true
                  favouriteAdapter.updateStatusChooseAll(isChooseAll)
              }else{
                  isChooseAll = false
                  favouriteAdapter.updateStatusChooseAll(isChooseAll)
              }
          }
      }
    }

    private fun setupRecycleView() {
        binding!!.apply {
            recycleViewDiscoverMovies.apply {
                favouriteAdapter = FavouriteAdapter(
                    isChooseAll,
                    isClickChoose,
                    object : FavouriteAdapter.OnClickItemListener{
                        override fun onClickItem(movie: FavouriteMovie) {
                            openScreen(DetailFragment.newInstance(movie.movie_id),true)
                        }
                    }
                )
                adapter = favouriteAdapter
                    layoutManager =
                    GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun displayFavouriteMovies() {
        lifecycleScope.launch {
            viewModel.favouriteMovies.observe(viewLifecycleOwner) { response->
                if(response != null && response.isNotEmpty()){
                    binding!!.apply {
                        titleEmpty.visibility = View.GONE
                        constraintLayout6.visibility = View.VISIBLE
                        recycleViewDiscoverMovies.visibility = View.VISIBLE
                        favouriteAdapter.updateMovies(response)
                    }
                }else{
                    binding!!.apply {
                        titleEmpty.visibility = View.VISIBLE
                        constraintLayout6.visibility = View.GONE
                        recycleViewDiscoverMovies.visibility = View.GONE
                    }
                }
            }

        }
    }
}
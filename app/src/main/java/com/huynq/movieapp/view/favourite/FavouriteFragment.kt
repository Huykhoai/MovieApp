package com.huynq.movieapp.view.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.util.query
import com.huynq.movieapp.adapter.FavouriteAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.databinding.FragmentFavouriteBinding
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.room.FavouriteMovie
import com.huynq.movieapp.utils.ConnectionLiveData
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.view.home.HomeFragment
import com.huynq.movieapp.viewmodel.FavouriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    private val viewModel: FavouriteMovieViewModel by viewModels()
    private lateinit var favouriteAdapter: FavouriteAdapter
    private var isClickChoose: Boolean = false
    private var isChooseAll: Boolean = false
    private var selectedList : MutableList<FavouriteMovie> = mutableListOf()
    private lateinit var cld: ConnectionLiveData


    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavouriteBinding {
        cld = ConnectionLiveData(requireActivity().application)
        return FragmentFavouriteBinding.inflate(layoutInflater,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        initView()
        initViewNetwork()
        lifecycleScope.launch {
            searchMovie()
        }
    }

    private fun initViewNetwork() {
        cld.observe(viewLifecycleOwner){ isConnected->
            if (isConnected){
                viewModel.favouriteMovies.observe(viewLifecycleOwner) { response->
                    handleMovieResponse(response, false)
                }
                viewModel.searchResult.observe(viewLifecycleOwner) { response ->
                    handleMovieResponse(response, true)
                }
            }else{
                showSuccessDialog("No Internet Connection")
            }
        }
    }

    private fun initView() {
      binding!!.apply {
          btnBack.setOnClickListener{
              openScreen(HomeFragment(),true)
          }
          requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
              object : OnBackPressedCallback(true) {
                  override fun handleOnBackPressed() {
                      isEnabled = false
                      openScreen(HomeFragment(),true)
                      return
                  }

              })
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

    private suspend fun searchMovie() {
        binding!!.searchMovie.getQueryTextChangeStateFlow()
            .debounce(300)
            .filter { query->
                if(query.isEmpty()){
                    withContext(Dispatchers.Main){
                        handleEmptyQuery(query)
                        }
                    return@filter false
                }else{
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                dataFromNetwork(query)
                    .catch {
                        emitAll(flowOf(""))
                    }
            }
            .flowOn(Dispatchers.Default)
            .collect{result->
                viewModel.getMovieByName(result)
            }
    }
    private fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String>{

        val query = MutableStateFlow("")

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query.value = newText
                return true
            }

        })
        return query
    }
    private fun dataFromNetwork(query: String) :Flow<String>{
        return flow {
            delay(300)
            emit(query)
        }
    }
    private suspend fun handleEmptyQuery(query: String) {
        if (query.isEmpty()) {
            binding!!.searchResultTextView.visibility = View.GONE
            viewModel.favouriteMovies.observe(viewLifecycleOwner) { response->
                handleMovieResponse(response, false)
            }
        }
    }
    private fun handleMovieResponse(response: List<FavouriteMovie>, isSearchResult: Boolean){
        binding!!.apply {
            if(response.isNotEmpty()){
                titleEmpty.visibility = View.GONE
                constraintLayout6.visibility = View.VISIBLE
                recycleViewDiscoverMovies.visibility = View.VISIBLE
                favouriteAdapter.updateMovies(response)
                if (isSearchResult) searchResultTextView.visibility = View.GONE
            }else{
                if(isSearchResult){
                    searchResultTextView.visibility = View.VISIBLE
                }else{
                    titleEmpty.visibility = View.VISIBLE
                    constraintLayout6.visibility = View.GONE
                }
                recycleViewDiscoverMovies.visibility = View.GONE
            }
        }
    }
}
package com.huynq.movieapp.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.huynq.movieapp.R
import com.huynq.movieapp.adapter.SearchAdapter
import com.huynq.movieapp.base.BaseFragment
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.databinding.FragmentSearchBinding
import com.huynq.movieapp.model.SearchResponse
import com.huynq.movieapp.model.SearchResult
import com.huynq.movieapp.utils.APIConstants
import com.huynq.movieapp.view.detail.DetailFragment
import com.huynq.movieapp.viewmodel.MainViewModel
import com.huynq.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchAdapter: SearchAdapter
    companion object{
        fun newInstance(query: String): SearchFragment{
            val fragment = SearchFragment()
            val args = Bundle()
            args.putString("query",query)
            fragment.arguments = args
            return fragment
        }
    }
    override fun getFragmentBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        val responsitory = MovieResponsitory()
        val mainViewModelFactory = MainViewModelFactory(responsitory)
        mainViewModel = ViewModelProvider(requireActivity(),mainViewModelFactory)[MainViewModel::class.java]
        binding?.lifecycleOwner = this
       return FragmentSearchBinding.inflate(layoutInflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupRecycleView()
        displaySearch()
    }

    private fun setupRecycleView() {
        binding?.recycleViewSearch?.layoutManager =
            GridLayoutManager(requireActivity(),2)
        searchAdapter = SearchAdapter(emptyList(), object : SearchAdapter.OnClickItemListener {
            override fun onClickItem(movie_id: Int) {
               openScreen(DetailFragment.newInstance(movie_id),true)
            }
        })
        binding?.recycleViewSearch?.adapter = searchAdapter
    }

    private fun initView() {
        binding?.btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    private fun displaySearch(){
        val query = arguments?.getString("query")
        if(query != null){
            lifecycleScope.launch {
                mainViewModel.searchMovies(query,APIConstants.API_KEY)
            }
        }
        mainViewModel.searchMoviesLiveData.observe(viewLifecycleOwner, Observer {
            if(it != null){
                lifecycleScope.launch {
                    updateSearch(it)
                }
            }
        })
    }
    suspend fun updateSearch(searchResult:SearchResponse?){
        withContext(Dispatchers.Main){
            if(searchResult != null && searchResult.results.isNotEmpty()){
               binding?.recycleViewSearch?.visibility = View.VISIBLE
                binding?.searchResultTextView?.visibility = View.GONE
                searchAdapter.listSearch = searchResult.results
                searchAdapter.notifyDataSetChanged()
            }else{
                binding?.searchResultTextView?.visibility = View.VISIBLE
                binding?.recycleViewSearch?.visibility = View.GONE
            }
        }

    }
}
//suspend fun searchMovie(){
//    binding.searchView.getQueryTextChangeStateFlow()
//        .debounce(300)
//        .filter { query ->
//            if (query.isEmpty()) {
//                withContext(Dispatchers.Main) {
//                    binding.searchResultTextView.visibility = View.GONE
//                    binding.searchResultRecyclerView.visibility = View.GONE
//                }
//                return@filter false
//            } else {
//                return@filter true
//            }
//        }
//        .distinctUntilChanged()
//        .flatMapLatest { query ->
//            dataFromNetwork(query)
//                .catch {
//                    emitAll(flowOf(""))
//                }
//        }
//        .flowOn(Dispatchers.Default)
//        .collect { result ->
//            mainViewModel.getSearchResults(result, APIConstants.API_KEY)
//            binding.searchResultRecyclerView.visibility = View.VISIBLE
//        }
//}
//
//private fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
//
//    val query = MutableStateFlow("")
//
//    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//        override fun onQueryTextSubmit(query: String?): Boolean {
//            return true
//        }
//
//        override fun onQueryTextChange(newText: String): Boolean {
//            query.value = newText
//            return true
//        }
//    })
//    return query
//}
//
//private fun dataFromNetwork(query: String): Flow<String> {
//    return flow {
//        delay(200)
//        emit(query)
//    }
//}
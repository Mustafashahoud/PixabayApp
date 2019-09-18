package com.mustafa.pixabayapp.ui.search

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mustafa.pixabayapp.AppExecutors
import com.mustafa.pixabayapp.R
import com.mustafa.pixabayapp.adapters.PhotoListAdapter
import com.mustafa.pixabayapp.databinding.FragmentSearchPhotoBinding
import com.mustafa.pixabayapp.di.Injectable
import com.mustafa.pixabayapp.utils.autoCleared
import kotlinx.android.synthetic.main.fragment_search_photo.*
import javax.inject.Inject

class SearchPhotoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var dataBindingComponent: DataBindingComponent

    var binding by autoCleared<FragmentSearchPhotoBinding>()

    var adapter by autoCleared<PhotoListAdapter>()

    lateinit var searchViewModel: SearchPhotoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_photo,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         searchViewModel = ViewModelProviders.of(
            this,
            viewModelFactory).get(SearchPhotoViewModel::class.java)

        binding.setLifecycleOwner (viewLifecycleOwner)

        initRecyclerView()

        val rvAdapter = PhotoListAdapter(
            appExecutors = appExecutors
        ) {
            Toast.makeText(activity, "We will do it later", Toast.LENGTH_LONG).show()
        }

        binding.query = searchViewModel.query
        binding.photoList.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()
    }

    private fun initSearchInputListener() {
        binding.searchView.setOnQueryTextListener (object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                doSearch(search_view, query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun doSearch(v: View, query: String) {
//        val query = binding.searchView.query.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initRecyclerView() {

        binding.photoList.layoutManager = LinearLayoutManager(activity)
        binding.photoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    Toast.makeText(activity, "We will do it later", Toast.LENGTH_LONG).show()
                }
            }
        })
        binding.searchResult = searchViewModel.results
        searchViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })

//        searchViewModel.loadMoreStatus.observe(viewLifecycleOwner, Observer { loadingMore ->
//            if (loadingMore == null) {
//                binding.loadingMore = false
//            } else {
//                binding.loadingMore = loadingMore.isRunning
//                val error = loadingMore.errorMessageIfNotHandled
//                if (error != null) {
//                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
//                }
//            }
//        })
    }




}
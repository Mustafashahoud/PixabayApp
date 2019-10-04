package com.mustafa.pixabayapp.ui.search

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mustafa.pixabayapp.AppExecutors
import com.mustafa.pixabayapp.R
import com.mustafa.pixabayapp.adapters.PhotoListAdapter
import com.mustafa.pixabayapp.databinding.FragmentSearchPhotoBinding
import com.mustafa.pixabayapp.di.Injectable
import com.mustafa.pixabayapp.models.Status
import com.mustafa.pixabayapp.ui.common.RetryCallback
import com.mustafa.pixabayapp.utils.autoCleared
import kotlinx.android.synthetic.main.fragment_search_photo.*
import javax.inject.Inject

class SearchPhotoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentSearchPhotoBinding>()

    var adapter by autoCleared<PhotoListAdapter>()

    lateinit var searchViewModel: SearchPhotoViewModel


    companion object {
        private const val DEFAULT_QUERY: String = "fruits"
    }


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
            viewModelFactory
        ).get(SearchPhotoViewModel::class.java)

        binding.setLifecycleOwner(viewLifecycleOwner)

        initRecyclerView()

        val rvAdapter = PhotoListAdapter(
            appExecutors = appExecutors
        ) { photo ->
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    findNavController().navigate(SearchPhotoFragmentDirections.showPhoto(photo.id))
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        binding.query = searchViewModel.query
        binding.photoListRecyclerView.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()


        binding.callback = object : RetryCallback {
            override fun retry() {
                searchViewModel.refresh()
            }
        }

        /**
         *  This logic is because When Rotating the screen the activity will be destroyed
         *  and re-created and that means executing the initial query again
         */
        if (searchViewModel.isScreenRotated) {
            searchViewModel.isScreenRotated = false
            searchViewModel.executeSearch(DEFAULT_QUERY, 1)
        }

        searchViewModel.getPhotos().observe(viewLifecycleOwner, Observer { result ->
            if (result.data != null) {
                adapter.submitList(result.data)
            } else if (result.status == Status.ERROR) {
                adapter.submitList(null)
            }
        })
    }

    private fun initSearchInputListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
        searchViewModel.isPerformingNextQuery = false
        dismissKeyboard(v.windowToken)
        searchViewModel.executeSearch(query, 1)
    }


    private fun initRecyclerView() {

        binding.photoListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.photoListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    binding.photoListRecyclerView.post {
                        searchViewModel.isPerformingNextQuery = true
                        searchViewModel.searchNextPage()
                    }
                }
            }
        })

        searchViewModel.loadMoreStatus.observe(viewLifecycleOwner, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        binding.searchResult = searchViewModel.photos

    }

    /**
     * dismiss Keyboard
     *
     * @param windowToken The token of the window that is making the request, as returned by View.getWindowToken().
     */
    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}
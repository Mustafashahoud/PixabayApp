package com.mustafa.pixabayapp.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mustafa.pixabayapp.R
import com.mustafa.pixabayapp.databinding.FragmentPhotoBinding
import com.mustafa.pixabayapp.di.Injectable
import com.mustafa.pixabayapp.models.Photo
import com.mustafa.pixabayapp.utils.autoCleared

class PhotoFragment : Fragment(), Injectable {

    var binding by autoCleared<FragmentPhotoBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_photo,
            container,
            false
        )

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.photo = getIncomingFragmentBundle()
    }


    private fun getIncomingFragmentBundle () : Photo {
        val bundle = arguments
        return bundle?.getSerializable("Photo") as Photo


    }

}
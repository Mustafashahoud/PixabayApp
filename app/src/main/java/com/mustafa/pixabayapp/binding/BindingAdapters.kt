package com.mustafa.pixabayapp.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mustafa.pixabayapp.R

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun showPhoto (imageView: ImageView, url : String) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.white_background)
            .into(imageView)
    }
}
package com.example.drawai

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.drawai.domain.Art
import com.example.drawai.gallery.GalleryAdapter
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}

@BindingAdapter("navigationOnClick")
fun MaterialToolbar.setNavigationOnClickListener(listener: () -> Unit) {
    setNavigationOnClickListener { listener() }
}

@BindingAdapter("artItems")
fun RecyclerView.setArtItems(arts: List<Art>?) {
    arts?.let {
        (adapter as? GalleryAdapter)?.submitList(it)
    }
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.setRefreshing(isRefreshing: Boolean?) {
    isRefreshing?.let { this.isRefreshing = it }
}

@BindingAdapter("formattedDate")
fun TextView.setFormattedDate(timestamp: Long?) {
    timestamp?.let {
        text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(it))
    }
}

@BindingAdapter("visibleIf")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("loadingState")
fun ProgressBar.bindLoadingState(state: Boolean?) {
    visibility = if (state == true) View.VISIBLE else View.GONE
}

@BindingAdapter("errorText")
fun TextView.bindErrorText(message: String?) {
    text = message ?: ""
    visibility = if (message.isNullOrEmpty()) View.GONE else View.VISIBLE
}
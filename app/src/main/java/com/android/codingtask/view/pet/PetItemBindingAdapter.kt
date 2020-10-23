package com.android.codingtask.view.pet

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android.codingtask.R
import com.android.codingtask.image.ImageLoader

@BindingAdapter("petImage")
fun petImage(imageView: ImageView, url: String) {
    // set placeholder image
    imageView.setImageResource(R.drawable.ic_placeholder_image)
    ImageLoader.displayImage(url, imageView)
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

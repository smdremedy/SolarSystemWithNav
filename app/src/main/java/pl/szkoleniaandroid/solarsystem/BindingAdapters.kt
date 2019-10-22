package pl.szkoleniaandroid.solarsystem

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("srcUrl")
fun setSrcUrl(imageView: ImageView, url: String) {
    imageView.load(url) {
        placeholder(R.drawable.planet_placeholder)
    }
}
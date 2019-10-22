package pl.szkoleniaandroid.solarsystem

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import coil.api.load
import kotlinx.android.synthetic.main.object_details_content.*

@BindingAdapter("srcUrl")
fun setSrcUrl(imageView: ImageView, url: String) {
    imageView.load(url) {
        placeholder(R.drawable.planet_placeholder)
    }
}

@BindingAdapter("htmlFromFile")
fun TextView.htmlFromFile(fileUrl: String){

    val textWithHtml = context.loadStringFromAssets(fileUrl)
    text = HtmlCompat.fromHtml(textWithHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

}
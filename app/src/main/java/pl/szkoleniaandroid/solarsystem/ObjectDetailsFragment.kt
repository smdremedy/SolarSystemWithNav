package pl.szkoleniaandroid.solarsystem

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import kotlinx.android.synthetic.main.object_details_content.*
import kotlinx.android.synthetic.main.object_details_fragment.*
import java.io.IOException


class ObjectDetailsFragment : Fragment() {


    private val args = navArgs<ObjectDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.object_details_fragment, container, false)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val solarObject = args.value.solarObject
        toolbar.title = solarObject.name
        toolbar_layout.title = solarObject.name
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            //What to do on back clicked
            findNavController().navigateUp()
        }

        if (!solarObject.video.isNullOrEmpty()) {
            fab.setOnClickListener { watchYoutubeVideo(solarObject.video!!) }
        } else {
            val p = fab.layoutParams as CoordinatorLayout.LayoutParams
            p.anchorId = View.NO_ID
            fab.layoutParams = p
            fab.isVisible = false
        }

        try {
            val textWithHtml = SolarObject.loadStringFromAssets(requireContext(), solarObject.text)
            detailsTextView.text = Html.fromHtml(textWithHtml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        detailImageView.load("file:///android_asset/" + solarObject.image) {
            placeholder(R.drawable.planet_placeholder)
        }

        val moons = solarObject.moons
        val hasMoons = moons.isNotEmpty()
        moonsLabel.visibility = if (hasMoons) View.VISIBLE else View.GONE
        moonsRecycleView.visibility = if (hasMoons) View.VISIBLE else View.GONE
        if (hasMoons) {
            val planetsAdapter = SolarObjectsAdapter()
            planetsAdapter.objectClickedListener = object : ObjectClickedListener {
                override fun objectClicked(clickedObject: SolarObject) {
                    findNavController().navigate(MainDirections.showObjectDetails(clickedObject))
                }
            }
            planetsAdapter.setObjects(moons.toTypedArray())
            moonsRecycleView.adapter = planetsAdapter
            moonsRecycleView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            //https://code.google.com/p/android/issues/detail?id=177613
            moonsRecycleView.isNestedScrollingEnabled = false
        }
    }

    private fun watchYoutubeVideo(id: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            //no YouTube app - show in browser
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id")
            )
            startActivity(intent)
        }
    }
}

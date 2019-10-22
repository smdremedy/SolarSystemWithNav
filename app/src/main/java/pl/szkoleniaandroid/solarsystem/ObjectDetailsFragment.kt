package pl.szkoleniaandroid.solarsystem

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import kotlinx.android.synthetic.main.object_details_content.*
import kotlinx.android.synthetic.main.object_details_fragment.*
import pl.szkoleniaandroid.solarsystem.databinding.ObjectDetailsFragmentBinding

class ObjectDetailsFragment : Fragment() {

    private val args by navArgs<ObjectDetailsFragmentArgs>()
    lateinit var binding: ObjectDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ObjectDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.so = args.solarObject

        setupToolbar()
        loadDetails()
        showMoons()
    }

    private fun setupToolbar() {
        //binding.toolbar.title = args.solarObject.name
        //toolbar_layout.title = args.solarObject.name
        //hack if we want to use toolbar without setting as support
        toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            //What to do on back clicked
            findNavController().navigateUp()
        }
    }

    private fun loadDetails() {
        if (!args.solarObject.video.isNullOrEmpty()) {
            fab.setOnClickListener { watchYoutubeVideo(args.solarObject.video!!) }
        } else {
            val p = fab.layoutParams as CoordinatorLayout.LayoutParams
            p.anchorId = View.NO_ID
            fab.layoutParams = p
            fab.isVisible = false
        }
        val textWithHtml = requireContext().loadStringFromAssets(args.solarObject.textFilename)
        detailsTextView.text = HtmlCompat.fromHtml(textWithHtml, FROM_HTML_MODE_LEGACY)


    }


    private fun showMoons() {
        moonsLabel.visibility = args.solarObject.hasMoons.asVisibility
        moonsRecycleView.visibility = args.solarObject.hasMoons.asVisibility
        if (args.solarObject.hasMoons) {
            moonsRecycleView.apply {
                adapter = SolarObjectsAdapter().apply {
                    objectClickedListener = object : ObjectClickedListener {
                        override fun objectClicked(clickedObject: SolarObject) {
                            showSolarObjectDetails(clickedObject)
                        }
                    }
                    setObjects(args.solarObject.moons)
                }
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                //https://code.google.com/p/android/issues/detail?id=177613
                isNestedScrollingEnabled = false
            }
        }
    }

    private fun showSolarObjectDetails(clickedObject: SolarObject) {
        findNavController().navigate(
            MainDirections.showObjectDetails(
                clickedObject
            )
        )
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

val Boolean.asVisibility get() = if (this) View.VISIBLE else View.GONE

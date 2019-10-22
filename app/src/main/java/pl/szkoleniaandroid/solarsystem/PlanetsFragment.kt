package pl.szkoleniaandroid.solarsystem


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.solar_objects_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class PlanetsFragment : SolarObjectsFragment() {
    override fun createObjects() = repository.planets
}

val Fragment.repository: SolarObjectsRepository
    get() = (requireActivity() as SolarSystemActivity).objectsRepository


abstract class SolarObjectsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.solar_objects_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectsRecyclerView.adapter = SolarObjectsAdapter().apply {
            setObjects(createObjects())
            objectClickedListener = object: ObjectClickedListener {
                override fun objectClicked(clickedObject: SolarObject) {
//                    val args = ObjectDetailsFragmentArgs.Builder(clickedObject).build()
//                    findNavController().navigate(R.id.nav_object_details_fragment, args.toBundle())
//
                    val direction = ObjectDetailsFragmentDirections.navDetails(clickedObject)
                    findNavController().navigate(direction)
                }

            }
        }


    }

    abstract fun createObjects(): Array<SolarObject>


}

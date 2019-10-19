package pl.szkoleniaandroid.solarsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.solar_objects_fragment.*

abstract class SolarObjectsFragment : Fragment() {

    private lateinit var solarObjectsAdapter: SolarObjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.solar_objects_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        solarObjectsAdapter = SolarObjectsAdapter()
        solarObjectsAdapter.setObjects(createObjects())
        solarObjectsAdapter.objectClickedListener = object : ObjectClickedListener {
            override fun objectClicked(clickedObject: SolarObject) {
                val args = ObjectDetailsFragmentArgs.Builder(clickedObject).build()
                findNavController().navigate(R.id.nav_details, args.toBundle())
            }
        }
        objectsRecyclerView.adapter = solarObjectsAdapter
        objectsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        solarObjectsAdapter.objectClickedListener = null
    }

    abstract fun createObjects(): Array<SolarObject>
}

class SolarObjectsAdapter : RecyclerView.Adapter<SolarObjectsViewHolder>(),
    ObjectClickedListener {

    var objectClickedListener: ObjectClickedListener? = null

    private val objects = mutableListOf<SolarObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolarObjectsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.solar_object_item, parent, false)
        return SolarObjectsViewHolder(itemView, this)
    }

    override fun getItemCount() = objects.size

    override fun onBindViewHolder(holder: SolarObjectsViewHolder, position: Int) {
        holder.setSolarObject(objects[position])
    }

    override fun objectClicked(clickedObject: SolarObject) {
        objectClickedListener?.objectClicked(clickedObject)
    }

    fun setObjects(objectsToSet: Array<SolarObject>) {
        objects.clear()
        objects.addAll(objectsToSet)
        notifyDataSetChanged()
    }
}

interface ObjectClickedListener {
    fun objectClicked(clickedObject: SolarObject)
}

class SolarObjectsViewHolder(
    itemView: View,
    private val objectClickedListener: ObjectClickedListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var solarObject: SolarObject

    private val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
    private val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)

    init {
        itemView.setOnClickListener { objectClickedListener.objectClicked(solarObject) }
    }

    fun setSolarObject(solarObject: SolarObject) {
        this.solarObject = solarObject

        itemTextView.text = solarObject.name
        itemImageView.load(solarObject.imagePath) {
            placeholder(R.drawable.planet_placeholder)
        }
    }
}


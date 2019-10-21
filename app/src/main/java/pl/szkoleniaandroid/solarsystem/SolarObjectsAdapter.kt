package pl.szkoleniaandroid.solarsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load

class SolarObjectsAdapter : RecyclerView.Adapter<SolarObjectsViewHolder>(),
    ObjectClickedListener {

    var objectClickedListener: ObjectClickedListener? = null

    private val objects = mutableListOf<SolarObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolarObjectsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.solar_object_item, parent, false)
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


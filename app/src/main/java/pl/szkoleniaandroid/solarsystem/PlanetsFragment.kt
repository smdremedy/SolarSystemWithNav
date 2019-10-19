package pl.szkoleniaandroid.solarsystem

import androidx.navigation.fragment.navArgs

class PlanetsFragment : SolarObjectsFragment() {

    private val args: PlanetsFragmentArgs by navArgs()

    override fun createObjects(): Array<SolarObject> {
        return args.planets ?: repository.planets
    }

    companion object {
        fun newInstance(objects: Array<SolarObject>) =
            PlanetsFragment().apply {
                arguments = PlanetsFragmentArgs.Builder()
                    .setPlanets(objects)
                    .build()
                    .toBundle()
            }
    }
}
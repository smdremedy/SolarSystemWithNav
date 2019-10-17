package pl.szkoleniaandroid.solarsystem

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.solar_system_activity.*

class SolarSystemActivity : AppCompatActivity(), MoonsFragment.Callback {

    lateinit var repository: ObjectsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solar_system_activity)
        setSupportActionBar(toolbar)
        repository = ObjectsRepository(this)

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.nav_details) {
                hideToolbar()
            } else {
                showToolbar()
            }

        }
        bottomNavigation.setupWithNavController(navController)
    }

    fun hideToolbar() {
        app_bar_layout.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
    }

    fun showToolbar() {
        app_bar_layout.visibility = View.VISIBLE
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun showTabs(viewPager: ViewPager) {
        tab_layout.visibility = View.VISIBLE
        tab_layout.setupWithViewPager(viewPager)
    }

    override fun hideTabs() {
        tab_layout.removeAllTabs()
        tab_layout.setOnTabSelectedListener(null)
        tab_layout.visibility = View.GONE
    }
}

val Fragment.repository get() = (requireActivity() as SolarSystemActivity).repository

class ObjectsRepository(context: Context) {
    val planets: List<SolarObject> = SolarObject.loadArrayFromJson(context, "planets")
    val others: List<SolarObject> = SolarObject.loadArrayFromJson(context, "others")
    val objectsWithMoons: List<SolarObject>

    init {
        objectsWithMoons = mutableListOf()
        for (planet in planets) {
            if (planet.hasMoons) {
                objectsWithMoons.add(planet)
            }
        }
        for (other in others) {
            if (other.hasMoons) {
                objectsWithMoons.add(other)
            }
        }
    }
}

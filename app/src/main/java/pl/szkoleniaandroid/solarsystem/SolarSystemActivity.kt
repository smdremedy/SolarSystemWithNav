package pl.szkoleniaandroid.solarsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.solar_system_activity.*

class SolarSystemActivity : AppCompatActivity(), MoonsFragment.Callback {

    val repository by lazy { SolarObjectsRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solar_system_activity)
        setSupportActionBar(toolbar)
        setupNavController()
    }

    private fun setupNavController() {
        findNavController(R.id.nav_host_fragment).apply {
            addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.nav_details) {
                    updateNavigationUiVisibility(visible = false)
                } else {
                    updateNavigationUiVisibility(visible = true)
                }
            }
            bottomNavigation.setupWithNavController(this)
        }
    }

    private fun updateNavigationUiVisibility(visible: Boolean) {
        app_bar_layout.visibility = visible.asVisibility
        window.decorView.post {
            bottomNavigation.visibility = visible.asVisibility
        }
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

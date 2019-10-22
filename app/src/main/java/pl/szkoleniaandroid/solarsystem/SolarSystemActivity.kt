package pl.szkoleniaandroid.solarsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.solar_system_activity.*

class SolarSystemActivity : AppCompatActivity() {

    val objectsRepository: SolarObjectsRepository by lazy { SolarObjectsRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solar_system_activity)

        //setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(toolbar, navController)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}

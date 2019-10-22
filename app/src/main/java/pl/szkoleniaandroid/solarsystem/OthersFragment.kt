package pl.szkoleniaandroid.solarsystem

class OthersFragment : SolarObjectsFragment() {
    override fun createObjects() = repository.others
}

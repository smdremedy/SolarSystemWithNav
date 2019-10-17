package pl.szkoleniaandroid.solarsystem

class OthersFragment : SolarObjectsFragment() {

    override fun createObjects(): Array<SolarObject> {
        return repository.others.toTypedArray()
    }
}
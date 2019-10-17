package pl.szkoleniaandroid.solarsystem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


class MoonsFragment : Fragment() {

    private var adapter: PlanetsWithMoonPagerAdapter? = null

    private var callback: Callback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.moons_fragment, container, false)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlanetsWithMoonPagerAdapter(fragmentManager!!, repository.objectsWithMoons)

        val pager = view.findViewById<ViewPager>(R.id.viewpager)
        pager.adapter = adapter
        callback?.showTabs(pager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback?.hideTabs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback
    }


    override fun onDetach() {
        super.onDetach()
        callback = null
    }


    interface Callback {
        fun showTabs(viewPager: ViewPager)

        fun hideTabs()
    }

}


class PlanetsWithMoonPagerAdapter(
    fm: FragmentManager,
    private val planetsWithMoon: List<SolarObject>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = planetsWithMoon.size


    override fun getItem(position: Int): Fragment {

        var moons = planetsWithMoon[position].moons

        return PlanetsFragment.newInstance(moons.toTypedArray())
    }

    override fun getPageTitle(position: Int): CharSequence {
        return planetsWithMoon[position].name
    }
}

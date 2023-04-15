package com.zero.golgol.fragment

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.R
import com.zero.golgol.adapter.ActivityAdapter
import com.zero.golgol.adapter.ActivityDetailAdapter
import com.zero.golgol.adapter.EventAdapter
import com.zero.golgol.databinding.FragmentExploreBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Activity
import com.zero.golgol.model.ActivityDetails
import com.zero.golgol.model.Event
import com.zero.golgol.utils.Utils

class ExploreFragment : BaseFragment(), ClickListener {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ActivityAdapter
    private lateinit var activitiesList: MutableList<Activity>

    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventsList: MutableList<Event>

    private lateinit var funPlayAdapter: ActivityDetailAdapter
    private lateinit var funPlayList: MutableList<ActivityDetails>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSortByActivity.isActivated = true
        binding.ivBack.isEnabled = false
        setUpRecyclerView()
        loadActivities()
        loadEvents()
        clickListeners()
    }

    private fun loadActivityDetails(activityName : String) {
        FirebaseDatabase.getInstance().getReference("${Utils.FunPlay}/$activityName").get().addOnSuccessListener {
           if(it.exists()){
               for( c in it.children){
                   val funPlay = c.getValue(ActivityDetails :: class.java)
                   if (funPlay != null)
                       funPlayList.add(funPlay)
               }
               funPlayAdapter.updateItem(funPlayList)
           }
       }
    }

    private fun loadEvents() {
       FirebaseDatabase.getInstance().getReference("events")
           .get().addOnSuccessListener {
               if(it.exists()){
                   for(e in it.children){
                       val events = e.getValue(Event::class.java)
                       if (events != null)
                           eventsList.add(events)
                   }
                   eventAdapter.updateItem(eventsList)
               }
           }
    }

    private fun setUpRecyclerView(){
        eventsList = mutableListOf()
        eventAdapter = EventAdapter(requireContext(), eventsList, this)
        binding.rvEvents.setHasFixedSize(true)
        binding.rvEvents.layoutManager =  LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        funPlayList = mutableListOf()
        funPlayAdapter = ActivityDetailAdapter(requireContext(), funPlayList, this)
        binding.rvActivityDetails.setHasFixedSize(true)
        binding.rvActivityDetails.layoutManager =  LinearLayoutManager(requireContext())
        binding.rvActivityDetails.adapter = funPlayAdapter
    }

    private fun loadActivities() {
        activitiesList = mutableListOf(
            Activity(
                R.drawable.img_activity_1, "Aerobic Dance",false),
            Activity(
                R.drawable.img_activity_2, "Swimming", false),
            Activity(
                R.drawable.img_activity_3, "Walking", false),
            Activity(
                R.drawable.img_activity_4, "Badminton", false),
            Activity(
                R.drawable.img_activity_5, "Baseball", false),
            Activity(
                R.drawable.img_activity_6, "Basketball", false),
            Activity(
                R.drawable.img_activity_7, "Bicycling", false),
            Activity(
                R.drawable.img_activity_8, "Climbing", false),
            Activity(
                R.drawable.img_activity_9, "Cricket", false),
            Activity(
                R.drawable.img_activity_10, "Frisbee", false),
            Activity(
                R.drawable.img_activity_11, "Jogging", false),
            Activity(
                R.drawable.img_activity_12, "Jumping Rope", false))

        adapter = ActivityAdapter(requireContext(), activitiesList, this)
        binding.rvActivity.setHasFixedSize(true)
        binding.rvActivity.layoutManager =  StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvActivity.adapter = adapter
    }

    private fun clickListeners() {
        binding.tvFunPlay.setOnClickListener {
            showSelectedTab(ExploreMode.FUN_PLAY)
        }

        binding.tvEvents.setOnClickListener {
            showSelectedTab(ExploreMode.EVENTS)
        }

        binding.tvSortByActivity.setOnClickListener {
            showSelectedSortTab(SortMode.ACTIVITY)
        }

        binding.tvSortByDate.setOnClickListener {
            showSelectedSortTab(SortMode.DATE)
        }

        binding.ivBack.setOnClickListener {
            binding.ivBack.isEnabled = false
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.rvActivityDetails.visibility = View.GONE
            binding.rvActivity.visibility = View.VISIBLE
        }
    }

    private fun showSelectedSortTab(mode: SortMode) {
        binding.tvSortByActivity.isActivated = false
        binding.tvSortByDate.isActivated = false

        when(mode){
            SortMode.ACTIVITY -> binding.tvSortByActivity.isActivated = true
            SortMode.DATE -> binding.tvSortByDate.isActivated = true
        }
    }

    private fun showSelectedTab(mode: ExploreMode) {
        binding.ivBack.isEnabled = false
        binding.tvEvents.setBackgroundColor(Color.WHITE)
        binding.tvFunPlay.setBackgroundColor(Color.WHITE)
        binding.tvEvents.setTextColor(ResourcesCompat.getColor(resources, R.color.orange, null))
        binding.tvFunPlay.setTextColor(ResourcesCompat.getColor(resources, R.color.orange, null))
        binding.tvEvents.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_events_orange, 0, 0, 0)
        binding.tvFunPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_fun_play_orange, 0, 0, 0)

        if (mode == ExploreMode.FUN_PLAY){
            binding.tvFunPlay.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_explore_tabs, null)
            binding.tvFunPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_fun_play_white, 0, 0, 0)
            binding.tvFunPlay.setTextColor(Color.WHITE)

            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.rvEvents.visibility = View.GONE
            binding.rvActivityDetails.visibility = View.GONE
            binding.lnrSortOptionsContainer.visibility = View.VISIBLE
            binding.rvActivity.visibility = View.VISIBLE
        }
        else{
            binding.tvEvents.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_explore_tabs, null)
            binding.tvEvents.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_events_white, 0, 0, 0)
            binding.tvEvents.setTextColor(Color.WHITE)
            binding.ivSearch.visibility = View.GONE
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.rvActivity.visibility = View.GONE
            binding.rvActivityDetails.visibility = View.GONE
            binding.lnrSortOptionsContainer.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
        }
    }

    override fun buttonClick() {
        //
    }

    override fun buttonClick(position: Int, type: String) {
        if (type == "Activity"){
            binding.ivBack.isEnabled = true
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.rvActivity.visibility = View.GONE
            binding.rvActivityDetails.visibility = View.VISIBLE

            funPlayList.clear()
            loadActivityDetails(activitiesList[position].activityName)
        }
        else if (type == "Activity Details"){
//            startActivity(Intent(requireActivity(), SessionDetailsActivity::class.java))
        }
    }

    override fun buttonClick(position: Int) {

    }
}
enum class ExploreMode {
    FUN_PLAY, EVENTS
}

enum class SortMode {
    ACTIVITY, DATE
}
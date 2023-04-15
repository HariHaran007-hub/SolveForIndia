package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zero.golgol.R
import com.zero.golgol.adapter.ActivityAdapter
import com.zero.golgol.databinding.ActivityCreateSession1Binding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Activity

class CreateSession1Activity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityCreateSession1Binding

    private lateinit var adapter: ActivityAdapter
    private lateinit var activitiesList: MutableList<Activity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateSession1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadActivities()
        clickListeners()
    }



    private fun clickListeners() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun loadActivities() {
        activitiesList = mutableListOf(
            Activity(
                R.drawable.img_activity_1, "Aerobic Dance",true),
            Activity(
                R.drawable.img_activity_2, "Swimming", true),
            Activity(
                R.drawable.img_activity_3, "Walking", true),
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

        adapter = ActivityAdapter(this, activitiesList, this)
        binding.rvActivity.setHasFixedSize(true)
        binding.rvActivity.layoutManager =  StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvActivity.adapter = adapter
    }


    override fun buttonClick() {
        //
    }

    override fun buttonClick(position: Int, type: String) {
        if (type == "Activity"){
            val intent = Intent(this,CreateSession2Activity::class.java)
            intent.putExtra(SESSION_IMAGE,activitiesList[position].activityImage)
            intent.putExtra(SESSION_NAME,activitiesList[position].activityName)
            startActivity(intent)
            finish()
        }
    }

    override fun buttonClick(position: Int) {
        //
    }

    companion object{
        const val SESSION_IMAGE = "session_image"
        const val SESSION_NAME = "session_name"
    }
}
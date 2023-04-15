package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityCreateSession3Binding
import java.text.SimpleDateFormat
import java.util.*

class CreateSession3Activity : BaseActivity() {

    companion object{
        const val SESSION_TIME = "session_tme"
        const val SESSION_DATE = "session_date"
    }

    private lateinit var binding: ActivityCreateSession3Binding

    private var fromTime = "from"
    private var toTime = "to"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSession3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvEventDate.text = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(Calendar.getInstance().time.time)
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this,CreateSession4Activity::class.java)
            intent.putExtra(CreateSession1Activity.SESSION_NAME,getIntent().getStringExtra(CreateSession1Activity.SESSION_NAME))
            intent.putExtra(CreateSession1Activity.SESSION_IMAGE,getIntent().getIntExtra(CreateSession1Activity.SESSION_IMAGE,0))
            intent.putExtra(SESSION_TIME,binding.tvEventTime.text.toString())
            intent.putExtra(SESSION_DATE,binding.tvEventDate.text.toString())
            intent.putExtra(CreateSession2Activity.LAT,getIntent().getDoubleExtra(CreateSession2Activity.LAT,0.0))
            intent.putExtra(CreateSession2Activity.LONG,getIntent().getDoubleExtra(CreateSession2Activity.LONG,0.0))
            intent.putExtra(CreateSession2Activity.PLACE_NAME,getIntent().getStringExtra(CreateSession2Activity.PLACE_NAME))
            startActivity(intent)
            finish()

        }

        binding.ivBack.setOnClickListener { finish() }

        binding.ivShowDatePicker.setOnClickListener { binding.tvEventDate.performClick() }

        binding.ivShowTimePicker.setOnClickListener { binding.tvEventTime.performClick() }

        binding.tvEventDate.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            if (binding.flCalendarContainer.visibility == View.VISIBLE){
                binding.flCalendarContainer.visibility = View.GONE
                binding.ivShowDatePicker.setImageResource(R.drawable.img_arrow_right)
            }
            else {
                binding.flCalendarContainer.visibility = View.VISIBLE
                binding.ivShowDatePicker.setImageResource(R.drawable.img_arrow_down)
            }
        }

        binding.calendar.setCalendarListener(object : CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                //
            }

            override fun onFirstDateSelected(startDate: Calendar) {
                val simpleDateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
                val start = simpleDateFormat.format(startDate.time.time)

                binding.tvEventDate.text = start
                binding.tvEventDate.performClick()
            }
        })

        binding.tvEventTime.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            if (binding.lnrTimePicker.visibility == View.VISIBLE){
                binding.lnrTimePicker.visibility = View.GONE
                binding.ivShowTimePicker.setImageResource(R.drawable.img_arrow_right)
            }
            else {
                binding.lnrTimePicker.visibility = View.VISIBLE
                binding.ivShowTimePicker.setImageResource(R.drawable.img_arrow_down)
            }
        }

        binding.fromTimePicker.addOnDateChangedListener { displayed, _ ->
            val date = SimpleDateFormat("EEE dd MMM hh:mm a", Locale.getDefault()).parse(displayed)
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

            fromTime = time
            binding.tvEventTime.text = fromTime + " - " + toTime

            validate()
        }

        binding.toTimePicker.addOnDateChangedListener { displayed, _ ->
            val date = SimpleDateFormat("EEE dd MMM hh:mm a", Locale.getDefault()).parse(displayed)
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

            toTime = time
            binding.tvEventTime.text = fromTime + " - " + toTime

            validate()
        }
    }

    private fun validate() {
        if (fromTime != "from" && toTime != "to") {
            val from = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(fromTime)?.time
            val to = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(toTime)?.time

            binding.btnContinue.isEnabled =
                fromTime != "00:00 a.m." && toTime != "00:00 a.m." && to!! > from!!
        }
    }
}
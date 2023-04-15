package com.zero.golgol.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.databinding.ItemActivityDetailsBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.FunPlay

class FunPlayAdapter(private val context: Context, private var eventsList: MutableList<FunPlay>,
                     private val listener: ClickListener
): RecyclerView.Adapter<FunPlayAdapter.ViewHolder>() {

    private var _binding: ItemActivityDetailsBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemActivityDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventsList[position]

        Glide.with(context)
            .asDrawable()
            .load(event.activityImage)
            .into(binding.ivEvent)

        binding.tvOrganizerName.text = event.activityOrganizer
        binding.tvEventTitle.text = event.activityName
        binding.tvAvenueDistance.text = event.activityDistance
        binding.tvEventTime.text = event.activityTime
        binding.tvAvailability.text = event.activityAvailability

        binding.root.setOnClickListener {
            listener.buttonClick(position, "Activity Details")
        }
    }

    override fun getItemCount(): Int {
        Log.d("Main", "getItemCount: ${eventsList.size}")
        return eventsList.size
    }

    fun updateList(list : MutableList<FunPlay>){
        this.eventsList = list
    }

}
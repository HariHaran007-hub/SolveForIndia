package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.databinding.ItemEventBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Event

class EventAdapter(private val context: Context, private var eventsList: MutableList<Event>,
                   private val listener: ClickListener): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var _binding: ItemEventBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemEventBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventsList[position]

        Glide.with(context)
            .asDrawable()
            .load(event.eventImage)
            .into(binding.ivEvent)
        binding.tvOrganizerName.text = event.eventOrganizer
        binding.tvEventTitle.text = event.eventName
        binding.tvAvenueDistance.text = event.eventDistance
        binding.tvEventTime.text = event.eventTime
        binding.tvAvailability.text = event.eventAvailability
        binding.tvTicketCost.text = event.eventCost

        binding.root.setOnClickListener {
            listener.buttonClick(position, "Activity Details")
        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    fun updateItem(eventList : MutableList<Event>){
        this.eventsList = eventsList
        notifyDataSetChanged()
    }

}